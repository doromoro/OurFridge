import React from 'react'
import { Navigate } from 'react-router-dom'

const PublicRoute = ({ authenticated, component: Component }) => {
  return (
    authenticated ? <Navigate to='/' {...alert("이미 로그인이 되어있습니다.")} /> : Component
  )
}

export default PublicRoute