import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import InfoIcon from "@mui/icons-material/Info";
import Stack from "@mui/material/Stack";
import { DataGrid, GridToolbar } from "@mui/x-data-grid";
import LoadingCircle from "ui-component/user/LoadingCircle";
import Button from '@mui/material/Button';
import VisibilityIcon from '@mui/icons-material/Visibility';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import Paper from '@mui/material/Paper';
import Draggable from 'react-draggable';
import Typography from '@mui/material/Typography';

//import { useDemoData } from '@mui/x-data-grid-generator';

import Box from "@mui/material/Box";


// Helper functions
function createData(
  pr,
  id,
  name,
  displayName,
  emailAddress,
  type,
) {
  return { pr, id, name, displayName, emailAddress, type };
}

function PaperComponent(props) {
    return (
      <Draggable
        handle="#draggable-dialog-title"
        cancel={'[class*="MuiDialogContent-root"]'}
      >
        <Paper {...props} />
      </Draggable>
    );
  }

// ==============================|| Pull-Request PAGE ||============================== //

const AuthorPage = () => {
    const totalUsers = useSelector(state => state.data.allUser)
    const pullRequest = useSelector(state => state.data.pullRequest)
  const [pageSize, setPageSize] = useState(10);
  const [pageNum, setPageNum] = useState(0);
  const [buttonText, setButtonText] = useState("Show Inactive Users")
const [showActive, setShowActive] = useState(false)
  const [data, setData] = useState(useSelector(state => state.data.allUser).filter((filteredUser) => {
    return filteredUser.user.active == true
  }))
  const [open, setOpen] = React.useState(false);
  const [currentData, setCurrentData] = useState({});
 const [userPullRequest, setUserPullRequest] = useState([])
 const [userReviewer, setUserReviewer] = useState([])

  const columns = [
    {
        field: "info",
        headerName: "INFO",
        filterable: false,
        disableClickEventBubbling: true,
        sortable: false,
  
        renderCell: (params) => {
          const onClick = (e) => {
            setCurrentData(params.row.pr);
            setUserPullRequest(pullRequest.filter((filteredPull) => {
                return filteredPull.values.author.user.name == params.row.pr.user.name
            }))
            setUserReviewer(pullRequest.filter((element)=>{
                 return (element.values.reviewers.find((insideMap) => {
                    return insideMap.user.name == params.row.pr.user.name
                 }))
            }))
            
            handleClickOpen();
          };
  
          return (
            <Stack direction="row" spacing={2}>
              <Button
                sx={{ borderRadius: 25, color: "#21a2f6" }}
                variant="outlined"
                size="small"
                onClick={onClick}
              >
                <InfoIcon />
              </Button>
            </Stack>
          );
        },
        flex: 0.25,
      },

    {
      field: "name",
      headerName: "Name",
      flex: 0.5,
    },
    {
        field: "displayName",
        headerName: "Display Name",
        flex: 0.5,
      },
    {
      field: "emailAddress",
      headerName: "E-mail",
      flex: 1,
    },
    {
      field: "type",
      headerName: "Type",
      flex: 0.5,
    },
    

 
  ];

  console.log("userReviewer: ", userReviewer)

  const rows = data.map((pr) => {
    return createData(
      pr,
      pr.user.id,
      pr.user.name,
      pr.user.displayName,
      pr.user.emailAddress,
      pr.user.type,


    );
  });
  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleMore = () => {
    alert("use for all reviewers for this user.!")
  };
  const handleActiveButton = () =>{
        setShowActive((prev) => !prev)  
        setButtonText(`${showActive ? "Show Inactive Users" : "Show Active Users"}`)
        setData(totalUsers.filter((filteredUser) => showActive ? filteredUser.user.active == true : filteredUser.user.active == false))
  }

  console.log("Kişiye özel pr: ", userPullRequest)

  if (totalUsers.length <= 0) {
    return (
      <LoadingCircle/>
    );
  }
  if (open) {
    return (
        <Dialog
        open={open}
        onClose={handleClose}
        PaperComponent={PaperComponent}
        aria-labelledby="draggable-dialog-title"
      >
        <DialogTitle style={{ cursor: 'move', fontWeight:"bold", fontSize:"20px" }} id="draggable-dialog-title">
          {currentData.user.displayName || "Unknown"}
          <Typography component={'p'} variant={'body2'} style={{ cursor: 'move', fontSize:"15px", color:"grey", fontWeight:"normal",  }}>          {currentData.user.name || "Unknown"}
</Typography>
<Typography component={'p'} variant={'body2'} style={{ cursor: 'move', fontSize:"15px", color:"grey", fontWeight:"normal",  }}>          {currentData.user.emailAddress || "Unknown"}
</Typography>
        </DialogTitle>
       
        <DialogContent>
          <DialogContentText style={{fontWeight:"bolder", fontSize:"15px"}}>
          <DialogTitle style={{fontWeight:"bold", marginLeft:"0px", display:"inline-block", fontSize:"15px" }} id="draggable-dialog-title">
          Total Pull Request Count:
        </DialogTitle>
            {userPullRequest.length}
          </DialogContentText>
          <DialogContentText style={{fontWeight:"bolder", fontSize:"15px"}}>
          <DialogTitle style={{fontWeight:"bold", marginLeft:"0px", display:"inline-block", fontSize:"15px" }} id="draggable-dialog-title">
          Total Reviewer Count:
        </DialogTitle>
            {userReviewer.length}
          </DialogContentText>
          <DialogContentText>
            To check all reviewers for {currentData.user.name} user. click more button.
            <Button onClick={handleMore}>
            More
          </Button>
          </DialogContentText>
          
        </DialogContent>
        <DialogActions style={{display:"flex", justifyContent:"flex-start", alignContent:"center"}}>
          <Button onClick={handleClose}>
            Close
          </Button>
        </DialogActions>
      </Dialog>
    );
  }

  return (
    <>   
    <Button onClick={handleActiveButton} sx={{borderRadius:"10px"}} variant="contained" endIcon={<VisibilityIcon />}>
  {buttonText}
</Button>
      <Box m="20px 0 0 0" height="75vh" sx={{backgroundColor:"white", borderRadius:"20px", border:"0px solid black !important"}}>
      
        <DataGrid
        sx={{backgroundColor:"white", borderRadius:"20px", border:"0px solid black !important"}}
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
            filter: {
      filterModel: {
        items: [{ columnField: 'rating', operatorValue: '>', value: '2.5' }],
      },
    },
          }}
        ></DataGrid>
      </Box>
      </>
    
  );
};

export default AuthorPage;
