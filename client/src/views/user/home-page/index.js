import { Typography } from '@mui/material';
import React, { useEffect, useState } from 'react';
import { useSelector } from 'react-redux'
import { DataGrid, GridToolbar } from "@mui/x-data-grid";
import Box from '@mui/material/Box';
import axios from 'axios';
// CONTANTS
const columns = [
     {
        field: "author",
        headerName: "Author",
        flex: 0.75,
      },
      {
        field: "emailAddress",
        headerName: "E-mail",
        flex: 1.5,
      },
      {
        field: "create",
        headerName: "Created",
        flex: 0.75,
      },
      {
        field: "state",
        headerName: "State",
        flex: 0.5,
      },
      {
        field: "name",
        headerName: "Repository",
        flex: 0.75,
      },
      {
        field: "title",
        headerName: "Title",
        flex: 1,
      },
  
 
    
  ];
  
  function createData(id, author, emailAddress, create, state, name, title ) {
    return { id, author, emailAddress, create, state, name, title };
  }
  

// ==============================|| SAMPLE PAGE ||============================== //

const HomePage = () => {
  const [data, setData] = useState([])
  const [isRefresh, setIsRefresh] = useState(false);
    const [page, setPage] = React.useState(0);
  const [rowsPerPage, setRowsPerPage] = React.useState(25);
  const openData = useSelector( (state)  =>  state.openPR.openPullRequest)

  useEffect(() => {
    const getData = async ()=>{
      const responseData = await axios("http://localhost:8989/api/v1/get-data?query=values.open&condition=true")
      setData([...data,...(responseData.data)]);
      console.log("ReNDER oldu")
    }
    getData();

  }, []);
  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(+event.target.value);
    setPage(0);
  }
  const rows2= data.map((pr)=> {
    return createData(pr.id, pr.values.author.user.name, pr.values.author.user.emailAddress, new Date(pr.values.createdDate).toISOString().split('T')[0], pr.values.state, pr.values.fromRef.repository.name, pr.values.title )
}) 
console.log("ROWS_2: ", rows2)
const rows = openData.map((pr)=> {
        return createData(pr.id, pr.values.author.user.name, pr.values.author.user.emailAddress, new Date(pr.values.createdDate).toISOString().split('T')[0], pr.values.state, pr.values.fromRef.repository.name, pr.values.title )
    })

    if(rows2.length <=0){
      return
     
          <Stack sx={{ color: 'grey.500' }} spacing={2} direction="row">
      <CircularProgress color="secondary" />
    </Stack>
       
      
      
    }

    return(
        <Box m="20px">
        <Box
          m="40px 0 0 0"
          height="75vh"

        >
          <DataGrid
            
            rows={openData.length <=0 ? rows2 : rows}
            getRowId={openData.length <=0 ? rows2.id : rows.id}
            columns={columns}
            components={{ Toolbar: GridToolbar }}
          />
        </Box>
        
      </Box>
    )
};

export default HomePage;


/*


import SampleService from 'services/sample/SampleService';
import ServiceCaller from 'services/ServiceCaller';


const SamplePage = () => {
    const [data, setData] = useState([]);
    const [isSuccess, setSuccess] = useState(false);
    const getData = () => {
        let serviceCaller = new ServiceCaller();
        SampleService.getProducts(serviceCaller, '', (res) => {
            setSuccess(true);
            setData(res.products);
        }, (err) => {
            setSuccess(false);
            console.log(err);
        })
      }
    
      useEffect(() => {
        getData()
      }, []);    

    return(
        <Grid container spacing={gridSpacing}>
            <Grid item xs={12}>
                {data?.map((product) => (
                         <Typography variant="body2">
                            {product.title}
                        </Typography>
                    )
                )}:(
                    <Typography variant="body2">
                        No Product Data
                    </Typography>
                )
                
            
            </Grid>
        </Grid>
    )
};

*/