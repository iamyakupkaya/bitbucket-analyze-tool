import React from 'react'
import { RotatingLines } from "react-loader-spinner";
import Box from "@mui/material/Box";

const LoadingCircle = () => {
  return (
    <Box
    sx={{
      width: "100vw",
      height: "100vh",
      display: "flex",
      justifyContent: "center",
      alignItems: "center",
    }}
  >
    <RotatingLines
  strokeColor="#2196f3"
  strokeWidth="8"
  animationDuration="0.75"
  width="150"
  visible={true}
/>
  </Box>
  )
}

export default LoadingCircle;