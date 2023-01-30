import React from 'react'
import { Navigate } from 'react-router-dom'

const PrivateRoute = ({ authenticated, component: Component }) => {
  return (
    authenticated ? Component : <Navigate to='/login' {...alert('로그인 후 이용가능합니다.')} />
  )
}

export default PrivateRoute