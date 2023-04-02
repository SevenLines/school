import { makeAutoObservable } from 'mobx';
import { registerRequest, loginRequest } from '../services/auth';

class AuthStore {
  isAuth = false;
  isAdmin = false;
  token = '';
  userId = null;

  constructor() {
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('role');
    if (token) {
      this.isAuth = true;
      this.token = token;
      this.isAdmin = role === 'ADMIN';
      this.userId = Number(localStorage.getItem('user-id'));
    }
    makeAutoObservable(this);
  }

  get authToken() {
    return this.token;
  }

  logout() {
    this.isAdmin = false;
    this.token = '';
    this.isAuth = false;
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('user-id');
  }

  async login({ username, password }) {
    const { data: loginData } = await loginRequest({
      email: username,
      password,
    });
    if (!loginData.token) {
      // TODO: handle wrong auth
      // throw Error('invalid login');
    }
    this.isAuth = true;
    this.token = loginData.token;
    this.isAdmin = loginData.email === 'admin@a.ru';
    this.userId = Number(loginData.id);

    localStorage.setItem('token', this.token);
    localStorage.setItem('role', this.isAdmin ? 'ADMIN' : 'USER');
    localStorage.setItem('user-id', loginData.id);

    return this.token;
  }

  async register({
    password,
    name,
    secondName,
    lastName,
    city,
    schoolClass,
    phoneNumber,
    email,
  }) {
    await registerRequest({
      password,
      name,
      secondName,
      lastName,
      city,
      schoolClass,
      phoneNumber,
      email,
    });

    await this.login({
      username: email, 
      password
    });
  }
}

export default new AuthStore();
