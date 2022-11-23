import React, {useState, useEffect, useCallback} from "react";
import Box from "@mui/material/Box";
import { Link } from "react-router-dom";
import { Button } from "@mui/material";
import styled from "styled-components"
import axios from "axios";
import {useSelector, useDispatch } from 'react-redux'
import { getPullRequests } from "redux/pull_request/PullRequestSlice";
import LoadingCircle from "ui-component/user/LoadingCircle";



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
    width: 100%;
    height: 100%;
    backgroundColor:"red";
    


`;



const LoginPage = () => {
  const [show, setShow] = useState(false);
  const [data, setData] = useState([])
  const dispatch = useDispatch();
  useEffect(() => {
    const getData = async () => {
      const dataResponse = await axios("http://localhost:8989/api/v1/get-data")
      setData([...data, ...dataResponse.data])
      setShow(true);

    }
    getData();
   }, []);
   
   console.log("data son durum", data)

   const yaz = useSelector((state) => state)
console.log("satte", yaz)

   if(!show){
    return (
      <LoadingCircle/>
    );
   }

  return (
    <>
      <StyledBox>
        <Box>
            <Button onClick={()=>dispatch(getPullRequests(data))} ><StyledLink to="/user" >USER</StyledLink></Button>
        </Box>
        <Box>
            <Button id="disable" disabled><StyledLink  to="/user" disabled >ADMIN</StyledLink></Button>
        </Box>
        
      </StyledBox>
    </>
  );
};

export default LoginPage;
