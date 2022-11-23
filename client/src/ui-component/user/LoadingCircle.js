import React from 'react'
import { Vortex } from "react-loader-spinner";
import Box from "@mui/material/Box";

const LoadingCircle = () => {
  return (
    <Box
    sx={{
      width: "100%",
      height: "100%",
      display: "flex",
      justifyContent: "center",
      alignItems: "center",
    }}
  >
    <Vortex
      visible={true}
      height="250"
      width="250"
      ariaLabel="vortex-loading"
      wrapperStyle={{}}
      wrapperClass="vortex-wrapper"
      colors={["red", "green", "blue", "yellow", "orange", "purple"]} // 6 adet
    />
  </Box>
  )
}

export default LoadingCircle;