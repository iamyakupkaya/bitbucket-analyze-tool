import React, {useState, useEffect, useCallback} from "react";
import Box from "@mui/material/Box";
import { Link } from "react-router-dom";
import { Button } from "@mui/material";
import styled from "styled-components"
import axios from "axios";
import {useSelector, useDispatch } from 'react-redux'
import { getOpenPullRequests } from "redux/open_true/OpenPullRequestSlice";
import Stack from '@mui/material/Stack';
import LinearProgress from '@mui/material/LinearProgress';



const StyledBox = styled(Box)`
  height: 100vh;
  width: 100vw;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 10%;
  
  & #disable{
      cursor: not-allowed;
    }
  & Button{
    border-radius: 15px;
    width: 150px;
    box-shadow: 5px 2px 15px 2px black;
    
  
  }
`;

const StyledLink = styled(Link)`
    text-decoration: none;

    


`;



const LoginPage = () => {
  const [data, setData] = useState([]);
  const [show, setShow] = useState(false);
  const dispatch = useDispatch();
  const loadSurvayer = useCallback(async () => {
    const dataResponse = await axios("http://localhost:8989/api/v1/get-data?query=values.open&condition=true")
    dispatch(getOpenPullRequests(dataResponse.data));
}, [dispatch]);
  useEffect(() => {
      loadSurvayer();
      setShow(true)
    
   }, []);
   const count = useSelector(state => state)

   if(!show){
    return <Stack sx={{ width: '100%', color: 'grey.500' }} spacing={2}>
    <LinearProgress color="secondary" />
    <LinearProgress color="success" />
    <LinearProgress color="inherit" />
  </Stack>
   }
   console.log("Count", count)
  return (
    <>
      <StyledBox>
        <Box>
            <Button ><StyledLink to="/user" >USER</StyledLink></Button>
        </Box>
        <Box>
            <Button id="disable" disabled><StyledLink  to="/user" disabled >ADMIN</StyledLink></Button>
        </Box>
        
      </StyledBox>
    </>
  );
};

export default LoginPage;
