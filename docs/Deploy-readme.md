# Разворачивание на удаленном сервере

## Необходимые пакеты на сервере
- git
- nginx
- maven
- postgresql
- default-jre

## Склонировать репозитории
Переходим в папку `/var/www/`
```bash
cd /var/www/
```

Клонируем FE репозиторий (убедиться что в репозитории есть папка dist, именно в ней должен быть сближенный фронт для отдачи nginx-ом)
```bash
git clone https://github.com/manInit/school-page-backend
```
Клонируем BE репозиторий
```bash
git clone https://github.com/manInit/school-page-front
```
## Настройка nginx
 Перейти в конфиги доступных сайтов nginx
 ```bash
 cd /etc/nginx/sites-avaible
 ```
 Здесь я решил не менять дефолтный, поэтому просто копируем его чтобы не писать новый с нуля (новый конфиг назвал school)
 ```bash
 cp default school
 ```
 - Открываем для редактирования и удаляем из `listen` строчку с `default_server`
 - Меняем порт т.к `80` используется в дефолтном конфиге (я выбрал 3001)
 - Меняем `root` на путь до `dist` папки с фронтом (папка с билдом фронта, которая собирается комндой `npm run build`)
 - Т.к api запросы мы будем пересылать на BE (tomcat сервер) настроим редирект с помощью `location`

 Получится должно похоже на это
```conf
server {
        listen 3001;
        ...
        root /var/www/school-page-front/dist;
        location /api {
            proxy_pass http://localhost:8080;
        }
        ...
```
Теперь нужно слинковать файл в `sites-enabled` уже "включенные конфиги сайта"  (первый путь всегда должен быть абсолютным)
```bash
ln -s /etc/nginx/sites-avaible/school /etc/nginx/sites-enabled/school
```
И перезагружаем сервис nginx
```bash
systemctl restart nginx
```

 Теперь можно проверить что статика отдается зайдя на ваш публичный ip с выставленным портом.
 
 ## Настройка базы данных
 Для работы нужно создать БД с определенным названием (хранится в репе BE по пути `src/main/resources/application.properties`)
 
 Заходим под `posgres` пользователем
 ```bash
  su - postgres
 ```
 Создаем базу данных
 ```bash
 createdb school-page2
 ```
 Посмотреть существующие базы данных (в том числе их кодировку, важно убедиться чтобы была  UTF-8) можно зайдя в pg консоль
 ```bash
 psql
 ```
  ```bash
\l
 ```
 Если в списке есть БД с нужным именем, и кодировкой UTF-8, то БД готова к работе
 
BE при подключении к бд использует пользователя и пароль лежащий в application.properties, стоит также убедиться что они соответвуют настройке бд

 ## Запуск BE 
 Переходим в склонированный BE репозиторий
 В нем запускаем билд проекта с помощью `maven`
 ```bash
 mvn clean package
 ```
Если никаких ошибок нет, то замечательно теперь у нас есть папка `target` в которой лежат готовые к запуску `jar` файлы
Запустить можно с помощью команды 
```bash
java -jar school-page-backend-1.0-SNAPSHOT.jar
```
После успешного запуска (а запускается кстати на порту 8080) можно открыть сваггер по ссылке 
```YOUR_IP:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config```

Однако нам желательно запустить все это дело на фоне, чтобы не занимать консоль. Поэтому создадим сервис. 
Создадим файл конфигурации сервиса
```bash
sudo nano /etc/systemd/system/school.service
```
И заполним его примерно таким образом
```conf
[Unit]
Description=students project
After=syslog.target
After=network.target[Service]
User=root
Type=simple

[Service]
ExecStart=/usr/bin/java -jar /var/www/school-page-backend/target/school-page-backend-1.0-SNAPSHOT.jar
Restart=always
StandardOutput=syslog
StandardError=syslog
SyslogIdentifier=school

[Install]
WantedBy=multi-user.target
```

Теперь можно запустить сервис с помощью `systemctl`
```bash
systemctl start school
```
Проверим статус 
```bash
systemctl status school
```
Если все ок, то поздравляю, теперь все должно работать корректно
