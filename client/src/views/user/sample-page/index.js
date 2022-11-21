// material-ui
import React, { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux'
import InfoIcon from '@mui/icons-material/Info';
import Button from '@mui/material/Button';
import Stack from '@mui/material/Stack';
import {openDialogMethod} from "../../../redux/dialog/dialogSlice"


import FullScreenDialog from 'views/utilities/FullScreenDialog';

// material-ui

import { DataGrid, GridToolbar } from "@mui/x-data-grid";
import Box from '@mui/material/Box';

import axios from 'axios';


// COMPONENT



// CONTANTS

  

// ==============================|| SAMPLE PAGE ||============================== //

const SamplePage = () => {
  const dispatch = useDispatch()
  const [data, setData] = useState([])
    const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(25);
  const openData = useSelector( (state)  =>  state.openPR.openPullRequest)
  const [open, setOpen] = useState(false);
  const [selectedPR, setSelectedPR] = useState({})
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


  const columns = [
    {
      field: "info",
      headerName: "INFO",
      disableClickEventBubbling: true,
      sortable: false,
    
      renderCell: (params) => {
   
          const onClick = (e) => {
            console.log(params)
            const currentRow = params.row;
            //alert(JSON.stringify(currentRow, null, 4));
            setSelectedPR({...params.row.pr})
            setOpen(true)
            
          };
          
          return (
            <Stack direction="row" spacing={2}>
              <Button variant="outlined" color="error" size="small" onClick={onClick}>More</Button>
            </Stack>
          );},
      flex: 0.5,
    },
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
          field: "name",
          headerName: "Repository",
          flex: 0.75,
        },
        {
          field: "create",
          headerName: "Created",
          flex: 0.75,
        },
        {
          field: "updated",
          headerName: "Updated",
          flex: 0.75,
        },
        {
          field: "state",
          headerName: "State",
          flex: 0.5,
        },
        
        {
          field: "title",
          headerName: "Title",
          flex: 1,
        },
    
   
      
    ];
    
    function createData(pr, id, author, emailAddress, name, create, updated, state,  title ) {
      return {pr, id, author, emailAddress, name, create, updated, state,  title };
    }

  const rows2= data.map((pr)=> {
    return createData(pr,
    pr.id, pr.values.author.user.name, 
    pr.values.author.user.emailAddress, 
    pr.values.fromRef.repository.name, 
    new Date(pr.values.createdDate).toISOString().split('T')[0],
    new Date(pr.values.updatedDate).toISOString().split('T')[0], 
    pr.values.state,  pr.values.title )
}) 
const rows = openData.map((pr)=> {
        return  createData(pr,
          pr.id, pr.values.author.user.name, 
          pr.values.author.user.emailAddress, 
          pr.values.fromRef.repository.name, 
          new Date(pr.values.createdDate).toISOString().split('T')[0], 
          new Date(pr.values.updatedDate).toISOString().split('T')[0], 
          pr.values.state,  pr.values.title )
    })

    if(rows2.length <=0){
      return
          <Stack sx={{ color: 'grey.500' }} spacing={2} direction="row">
      <CircularProgress color="secondary" />
    </Stack>
       
      
      
    }

    if(open){
      return <FullScreenDialog data={{open:true, setOpen:setOpen, selectedPR:selectedPR}}/>
    }

    return(<Box m="20px">
    <Box
      m="40px 0 0 0"
      height="75vh"

    >

      <DataGrid
        rows={openData.length <=0 ? rows2 : rows}
        getRowId={openData.length <=0 ? rows2.id : rows.id}
        columns={columns}
        components={{ Toolbar: GridToolbar }}
        
      >
</DataGrid>

    </Box>
    
  </Box>)
        
    
};

export default SamplePage;


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