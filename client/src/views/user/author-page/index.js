import React, { useEffect, useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import InfoIcon from "@mui/icons-material/Info";
import Stack from "@mui/material/Stack";
import { DataGrid, GridToolbar } from "@mui/x-data-grid";
import LoadingCircle from "ui-component/user/LoadingCircle";
import IconButton from '@mui/material/IconButton';
import VisibilityIcon from '@mui/icons-material/Visibility';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import Paper from '@mui/material/Paper';
import Draggable from 'react-draggable';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import {getLastPage} from "../../../redux/pull_request/PullRequestSlice"
import Diversity2Icon from '@mui/icons-material/Diversity2';
import TextField from '@mui/material/TextField'; 
import FormControl from '@mui/material/FormControl';
//import { useDemoData } from '@mui/x-data-grid-generator';

import Box from "@mui/material/Box";
import PullRequestList from "ui-component/user/PullRequestList";
import ReviewerList from "ui-component/user/ReviewerList";
import ConfirmDialog from "ui-component/user/ConfirmDialog";



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
  const dispatch = useDispatch();
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
 const [userPullRequest, setUserPullRequest] = useState([]);
 const [userReviewer, setUserReviewer] = useState([]);
const [showMore, setShowMore] = useState(false);
const [showMoreReviewer, setShowMoreReviewer] = useState(false);
const [checkboxSelection, setCheckboxSelection] = useState(true);
const [teamButton, setTeamButton] = useState(false);
const [checkboxSelectedUsers, setCheckboxSelectedUsers] = useState([]);
const [teamText, setTeamText] = useState("");

useEffect(() => {
  dispatch(getLastPage("authors"))

  
}, [])

if(pullRequest.length <= 0 || !pullRequest){
  return (
    <ConfirmDialog/>
  ); 
}


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
              <IconButton onClick={onClick} sx={{ borderRadius: 25, color: "#21a2f6" }} aria-label="info">
  <InfoIcon />
</IconButton>
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
    setShowMore(true)
  };
  const handleMoreReviewers = () => {
    setShowMoreReviewer(true)
  };
  const handleActiveButton = () =>{
        setShowActive((prev) => !prev)  
        setButtonText(`${showActive ? "Show Inactive Users" : "Show Active Users"}`)
        setData(totalUsers.filter((filteredUser) => showActive ? filteredUser.user.active == true : filteredUser.user.active == false))
  }
  
  const handleTeamButton = () => {
    setTeamButton(true)
  }
  const handleSubmitTeam = ()=> {
    console.log("checkboxt: ", teamText)

    setCheckboxSelectedUsers([]);
    setTeamButton(false);
    setTeamText("");
  }
  

  if (totalUsers.length <= 0) {
      return (
        <LoadingCircle/>
      );
    
  }
  if(teamButton){
    return <Dialog
    open={teamButton}
    onClose={()=> setTeamButton(false)}
    aria-labelledby="alert-dialog-title"
    aria-describedby="alert-dialog-description"
  >
    <FormControl>
    <TextField sx={{mt:5, mb:2, ml:5,mr:5}} 
    id="outlined-basic" label="Team Name" variant="outlined" 
    onChange={(event)=> setTeamText(event.target.value)}
    defaultValue={teamText}
    />

</FormControl>
    <DialogActions>
      <Button onClick={()=> setTeamButton(false)}>Close</Button>
      <Button onClick={handleSubmitTeam}>
        Submit
      </Button>
    </DialogActions>
  </Dialog>
  }

  if(showMore){
    return <PullRequestList data={{ open: showMore, setOpen: setShowMore, selectedPR: userPullRequest }}/>
  }

  if(showMoreReviewer){
    return <ReviewerList data={{ open: showMoreReviewer, setOpen: setShowMoreReviewer, selectedPR: userReviewer, selectedUser:currentData }}/>
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
          <Typography  variant={'body2'} style={{ cursor: 'move', fontSize:"15px", color:"grey", fontWeight:"normal",  }}>          {currentData.user.name || "Unknown"}
</Typography>
<Typography  variant={'body2'} style={{ cursor: 'move', fontSize:"15px", color:"grey", fontWeight:"normal",  }}>          {currentData.user.emailAddress || "Unknown"}
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
          Total Reviewing Count:
        </DialogTitle>
            {userReviewer.length}
          </DialogContentText>
          <DialogContentText>
            To check all <Typography variant="h6" sx={{display:"inline"}} color="initial">pull requests</Typography> for <Typography variant="h5" sx={{display:"inline"}} color="initial">{currentData.user.name}</Typography> user click more button.
            <Button disabled={userPullRequest.length <=0 ? true : false} onClick={handleMore}>
            More
          </Button>
          </DialogContentText>
          <DialogContentText>
            To check all <Typography variant="h6" sx={{display:"inline"}} color="initial">reviewing</Typography> for <Typography variant="h5" sx={{display:"inline"}} color="initial">{currentData.user.name}</Typography> user click more button.
            <Button disabled={userReviewer.length <=0 ? true : false} onClick={handleMoreReviewers}>
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
    <Box sx={{display:"flex", justifyContent:"space-between"}}>
    <Button onClick={handleActiveButton} sx={{borderRadius:"10px"}} variant="contained" endIcon={<VisibilityIcon />}>
  {buttonText}
</Button>
<Button onClick={handleTeamButton} sx={{borderRadius:"10px"}} variant="contained" endIcon={<Diversity2Icon />}>ADD TEAM</Button>
    </Box>

      <Box m="20px 0 0 0" height="75vh" sx={{backgroundColor:"white", borderRadius:"20px", border:"0px solid black !important"}}>
      
        <DataGrid
                  checkboxSelection={checkboxSelection}

        sx={{backgroundColor:"white", borderRadius:"20px", border:"0px solid black !important"}}
          rows={rows}
          getRowId={rows.id}
          columns={columns}
          components={{ Toolbar: GridToolbar }}
          rowsPerPageOptions={[10, 25, 50, 75, 100]}
          pageSize={pageSize}
          onPageChange={(newPage) => setPageNum(newPage)}
          onPageSizeChange={(newPage) => setPageSize(newPage)}
          onSelectionModelChange={(ids) => {
            const selectedIDs = Array.from(new Set(ids))
            const selectedData = data.filter((element) => {
              return selectedIDs.find((idElement) => {
                return element.user.id == idElement
              })
            }
            )
            setCheckboxSelectedUsers(selectedData);

          }}
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
