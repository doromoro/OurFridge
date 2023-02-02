import React from 'react'
import axios from 'axios'
import { useForm } from 'react-hook-form'
import { useNavigate } from 'react-router-dom'

import { Stack, Form, Button } from 'react-bootstrap'


const ChangePw = () => {

  const navigate = useNavigate();
  
  



  return (
    <div className='mx-5 mt-5 mb-3'>
      <Stack direction="horizontal" gap={3}>
        <Form.Control className="me-auto" placeholder="Email" />
        <Button variant="secondary">Submit</Button>
        <div className="vr" />
        <Button variant="outline-danger">Reset</Button>
    </Stack>
    </div>
  )
}

export default ChangePw