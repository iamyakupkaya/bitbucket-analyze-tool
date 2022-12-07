import React, {useState} from 'react'
import { useNavigate } from "react-router-dom";
import ConfirmBox from "react-dialog-confirm";
import '../../../node_modules/react-dialog-confirm/build/index.css';
import LOGOIMG from "../../assets/images/orion_logo.png"
import styled from "styled-components";
import { blue } from '@mui/material/colors';

const ConfirmDialog = () => {
    let navigate = useNavigate();    
    const handleConfirm = () => { return navigate("/."); }
    const handleCancel = () => { alert('Please click YES to load data!'); }

  return (
    <ConfirmBox // Note : in this example all props are required
    options={{
      color:"$ff45dd",
      icon:LOGOIMG,
      text: `There is no data to show! Would you want to load data?`,
      confirm: 'YES',
      cancel: 'NO',
      btn: true,
      color:"blue",
      backgroundColor:"red"
    }}
    isOpen={true}
    onConfirm={handleConfirm}
    onCancel={handleCancel}
  />
  )
}

export default ConfirmDialog