import React from 'react'
import { RotatingLines } from "react-loader-spinner";
import Box from "@mui/material/Box";
import Typewriter from 'typewriter-effect';


const LoadingCircle = () => {

  


  return (
    <Box
    sx={{
      width: "100vw",
      height: "100vh",
      display: "flex",
      justifyContent: "center",
      alignItems: "center",
      flexDirection:"column"
    }}
  >
    <RotatingLines
  strokeColor="#616161"
  strokeWidth="8"
  animationDuration="0.75"
  width="150"
  visible={true}
/>
<br></br>
<br></br>

<Typewriter
  options={{
    autoStart:true,
    loop:true,
    delay:50,
    cursor:"<strong>|</strong>",
    strings:["<strong>Please wait data is loading..!</strong>"]
  }}
/>
  </Box>
  )
}

export default LoadingCircle;