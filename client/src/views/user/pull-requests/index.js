import React, { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux'
import InfoIcon from '@mui/icons-material/Info';
import Button from '@mui/material/Button';
import Stack from '@mui/material/Stack';
import FullScreenDialog from 'ui-component/user/FullScreenDialog';
import { DataGrid, GridToolbar } from "@mui/x-data-grid";
import { Vortex } from  'react-loader-spinner'
//import { useDemoData } from '@mui/x-data-grid-generator';

import Box from '@mui/material/Box';

import axios from 'axios';

// Helper functions
function createData(pr, id, author, emailAddress, name, create, updated, state,  title ) {
  return {pr, id, author, emailAddress, name, create, updated, state,  title };
}

// ==============================|| Pull-Request PAGE ||============================== //

const PullRequestPage = () => {
 
  const [data, setData] = useState([])
  const [pageSize, setPageSize] = useState(10);
  const [pageNum, setPageNum] = useState(0);
  const [open, setOpen] = useState(false);
  const [selectedPR, setSelectedPR] = useState({})

  useEffect(() => {
    const getData = async ()=>{
      const responseData = await axios("http://localhost:8989/api/v1/get-data?query=values.open&condition=true")
      setData([...data,...(responseData.data)]);
    }
    getData();

  }, []);

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
              <Button   sx={{ borderRadius:25, color:"#21a2f6"}}
 variant="outlined" size="small" onClick={onClick}><InfoIcon/></Button>
            </Stack>
          );},
      flex: 0.75,
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
    
const rows = data.map((pr)=> {
        return  createData(pr,
          pr.id, pr.values.author.user.name, 
          pr.values.author.user.emailAddress, 
          pr.values.fromRef.repository.name, 
          new Date(pr.values.createdDate).toISOString().split('T')[0], 
          new Date(pr.values.updatedDate).toISOString().split('T')[0], 
          pr.values.state,  pr.values.title )
    })

    if(data.length <=0){
      return (
        <Box sx={{ width:"100%", height:"100%", display:"flex", justifyContent: 'center', alignItems:"center" }}>
          <Vortex
            visible={true}
            height="250"
            width="250"
            ariaLabel="vortex-loading"
            wrapperStyle={{}}
            wrapperClass="vortex-wrapper"
            colors={['red', 'green', 'blue', 'yellow', 'orange', 'purple']}
          />
      </Box>
      )
         
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
        rows={rows}
        getRowId={rows.id}
        columns={columns}
        components={{ Toolbar: GridToolbar }}
        rowsPerPageOptions={[10, 25, 50, 75, 100]}
        pageSize={pageSize}
        onPageChange={(newPage) => setPageNum(newPage)}
        onPageSizeChange={(newPage) => setPageSize(newPage)}
        initialState={{
          pagination: {
            page: pageNum,
          },
        }}

        
      >
</DataGrid>

    </Box>
    
  </Box>)
        
    
};

export default PullRequestPage;


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