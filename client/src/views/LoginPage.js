import React, {useState, useEffect, useCallback} from "react";
import Box from "@mui/material/Box";
import { Link } from "react-router-dom";
import { Button } from "@mui/material";
import styled from "styled-components"
import axios from "axios";
import {useSelector, useDispatch } from 'react-redux'
import { getPullRequests } from "redux/pull_request/PullRequestSlice";
import LoadingCircle from "ui-component/user/LoadingCircle";
import { Navigate } from 'react-router-dom';


const LoginPage = () => {
  const [show, setShow] = useState(false);
  const dispatch = useDispatch();
  useEffect(() => {
    const getData = async () => {
      const dataResponse = await axios("http://localhost:8989/api/v1/get-data")
      dispatch(getPullRequests([...dataResponse.data]))
      setShow(true);

    }
    getData();
   }, []);
   

   if(!show){
    return (
      <>
      <LoadingCircle/>
      </>
            
      
    );
   }

  return (<Navigate to="/user" />)
};

export default LoginPage;
