import React from 'react';
import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useForm, Controller } from 'react-hook-form';
import { emailRegExp } from '../../utils/reg-exp';
import Toast from 'react-bootstrap/Toast';
import InputMask from 'react-input-mask';
import authStore from '../../store/auth';

import './index.scss';
import { Modal } from 'react-bootstrap';

const RegisterPage = () => {
  const {
    register,
    handleSubmit,
    control,
    formState: { errors },
  } = useForm();
  const [showToast, setShowToast] = useState(false);
  const navigate = useNavigate();

  const onSubmit = async (data) => {
    try {
      await authStore.register(data);
    } catch (e) {
      setShowToast(true);
      return;
    }
    navigate('/');
  };

  const modalOptions = {
    size: 'lg',
    backdrop: true,
    keyboard: true,
  };
  const [isModalActive, setModalActive] = useState(false);
  const modalHandleClose = () => setModalActive(false);
  const modalHandleShow = () => setModalActive(true);

  return (
    <div className='main-page mx-auto'>
      <div className='container mt-5 pb-5'>
        <div className='row'>
          <div className='border pt-3'>
            <h3 className='main-page__title text-center pt-3'>Регистрация</h3>
            <form onSubmit={handleSubmit(onSubmit)} className='mt-4'>
              <div>
                <input
                  {...register('secondName', {
                    required: true,
                    pattern: /^[А-Яа-я]+$/i,
                  })}
                  id='reg-second-name'
                  type='text'
                  className='school-input form-control'
                  placeholder='Фамилия...'
                />
                {errors.secondName && (
                  <div className='ms-2 invalid-feedback d-block'>
                    Введите фамилию
                  </div>
                )}
              </div>
              <div className='mt-3'>
                <input
                  {...register('name', {
                    required: true,
                    pattern: /^[А-Яа-я]+$/i,
                  })}
                  id='reg-first-name'
                  type='text'
                  className='school-input form-control'
                  placeholder='Имя...'
                />
                {errors.name && (
                  <div className='ms-2 invalid-feedback d-block'>
                    Введите имя
                  </div>
                )}
              </div>
              <div className='mt-3'>
                <input
                  {...register('lastName', {
                    required: true,
                    pattern: /^[А-Яа-я]+$/i,
                  })}
                  id='reg-middle-name'
                  type='text'
                  className='school-input form-control'
                  placeholder='Отчество...'
                />
                {errors.lastName && (
                  <div className='ms-2 invalid-feedback d-block'>
                    Введите отчество
                  </div>
                )}
              </div>
              <div className='mt-3'>
                <input
                  {...register('city', {
                    required: true,
                  })}
                  id='reg-city'
                  type='text'
                  className='school-input form-control'
                  placeholder='Населенный пункт...'
                />
                {errors.city && (
                  <div className='ms-2 invalid-feedback d-block'>
                    Введите город
                  </div>
                )}
              </div>
              <div className='mt-3'>
                <input
                  {...register('school', {
                    required: true,
                  })}
                  id='reg-school'
                  type='text'
                  className='school-input form-control'
                  placeholder='Наименование образовательного учреждения...'
                />
                {errors.school && (
                  <div className='ms-2 invalid-feedback d-block'>
                    Введите школу
                  </div>
                )}
              </div>
              <div className='mt-3'>
                <input
                  {...register('schoolClass', {
                    required: true,
                  })}
                  id='reg-class'
                  type='text'
                  className='school-input form-control'
                  placeholder='Класс...'
                />
                {errors.schoolClass && (
                  <div className='ms-2 invalid-feedback d-block'>
                    Введите класс
                  </div>
                )}
              </div>
              <div className='mt-3'>
                <Controller
                  name='phoneNumber'
                  control={control}
                  render={({ field: { onChange } }) => (
                    <InputMask
                      onChange={onChange}
                      alwaysShowMask
                      className='school-input form-control'
                      mask='+7 (999) 999-99-99'
                    />
                  )}
                />
                {errors.phoneNumber && (
                  <div className='ms-2 invalid-feedback d-block'>
                    Введите телефон
                  </div>
                )}
              </div>
              <div className='mt-3'>
                <input
                  {...register('email', {
                    required: true,
                    pattern: emailRegExp,
                  })}
                  id='login-email'
                  type='text'
                  className='school-input form-control'
                  placeholder='E-mail...'
                />
                {errors.email && (
                  <div className='ms-2 invalid-feedback d-block'>
                    Введите E-mail
                  </div>
                )}
              </div>
              <div className='mt-3'>
                <input
                  {...register('password', {
                    required: true,
                  })}
                  id='login-pass'
                  type='password'
                  className='school-input form-control'
                  placeholder='Пароль...'
                />
                {errors.password && (
                  <div className='ms-2 invalid-feedback d-block'>
                    Введите пароль
                  </div>
                )}
              </div>
              <div className='mt-3 school-checkbox-container'>
                <input
                  {...register('agreementCheck', {
                    required: true,
                  })}
                  className='form-check-input'
                  type='checkbox'
                  id='agreementCheck'
                />
                <label className='form-check-label' htmlFor='agreementCheck'>
                  Согласие на обработку персональных данных
                </label>
                <a href='#' onClick={modalHandleShow}>
                  Согласие
                </a>
              </div>
              {errors.agreementCheck && (
                <div className='ms-2 invalid-feedback d-block'>
                  Подтвердите соглашение
                </div>
              )}
              <div className='mt-3 d-flex justify-content-end'>
                <button className='btn btn-primary w-100'>
                  Зарегистрироваться
                </button>
              </div>
            </form>
            <p className='text-center mt-3'>
              Уже есть аккаунт? <Link to='/login'>Войти</Link>
            </p>
          </div>
        </div>
      </div>
      <Modal
        show={isModalActive}
        onHide={modalHandleClose}
        size={modalOptions.size}
        backdrop={modalOptions.backdrop}
        keyboard={modalOptions.keyboard}
        centered
      >
        <Modal.Header closeButton>
          <Modal.Title>Соглашение</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p>txt</p>
        </Modal.Body>
        <Modal.Footer></Modal.Footer>
      </Modal>
      <Toast
        className='mx-auto fixed-center bg-danger text-white text-center'
        onClose={() => setShowToast(false)}
        show={showToast}
        delay={3000}
        autohide
      >
        <Toast.Body>Такой пользователь уже существует</Toast.Body>
      </Toast>
    </div>
  );
};

export default RegisterPage;
