import React, { useEffect, useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import InfoIcon from "@mui/icons-material/Info";
import Stack from "@mui/material/Stack";
import { DataGrid, GridToolbar, GridLinkOperator } from "@mui/x-data-grid";
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
import FormControl from '@mui/material/FormControl';
import Box from "@mui/material/Box";
import PullRequestList from "../../../ui-component/user/PullRequestList";
import ReviewerList from "../../../ui-component/user/ReviewerList";
import ConfirmDialog from "../../../ui-component/user/ConfirmDialog";
import InputLabel from '@mui/material/InputLabel';
import { styled } from '@mui/material/styles';
import NativeSelect from '@mui/material/NativeSelect';
import InputBase from '@mui/material/InputBase';
import axios from "axios";
import { getPullRequests } from "../../../redux/pull_request/PullRequestSlice";

// Helper functions
function createData(
  pr,
  id,
  name,
  displayName,
  teamName,
  emailAddress,
 
) {
  return { pr, id, name, displayName,  teamName, emailAddress };
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


const BootstrapInput = styled(InputBase)(({ theme }) => ({
  'label + &': {
    marginTop: theme.spacing(3),
  },
  '& .MuiInputBase-input': {
    borderRadius: 4,
    position: 'relative',
    backgroundColor: theme.palette.background.paper,
    border: '1px solid #ced4da',
    fontSize: 16,
    padding: '10px 26px 10px 12px',
    transition: theme.transitions.create(['border-color', 'box-shadow']),
    // Use the system font instead of the default Roboto font.
    fontFamily: [
      '-apple-system',
      'BlinkMacSystemFont',
      '"Segoe UI"',
      'Roboto',
      '"Helvetica Neue"',
      'Arial',
      'sans-serif',
      '"Apple Color Emoji"',
      '"Segoe UI Emoji"',
      '"Segoe UI Symbol"',
    ].join(','),
    '&:focus': {
      borderRadius: 4,
      borderColor: '#80bdff',
      boxShadow: '0 0 0 0.2rem rgba(0,123,255,.25)',
    },
  },
}));


const teamNames = ["Unknown", "NRD1112", "DENEME12", "OCM45", "IST3434"];

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
const [selectedRows, setSelectedRows] = React.useState([]);


const updateDataWithTeam = (arr) => {
  let newData = (pullRequest.map((dataElement) => {
    let newTeamName = arr.find((element)=> {
      return element.user.id == dataElement.values.author.user.id
    })
        const newObj = {
          ...dataElement,
          values:{
            ...dataElement.values,
            author:{
              ...dataElement.values.author,
              teamName:newTeamName ? teamText : dataElement.values.author.teamName
            }
          }
        }
        return newObj;
      }))
  dispatch(getPullRequests([...newData]))
}

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
      field: "teamName",
      headerName: "Team Name",
      flex: 0.5,
    },
    {
      field: "emailAddress",
      headerName: "E-mail",
      flex: 1,
    },
    

 
  ];


  const rows = data.map((pr) => {
    return createData(
      pr,
      pr.user.id,
      pr.user.name,
      pr.user.displayName,
      pr.teamName,
      pr.user.emailAddress,



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
  
 
  const handleSubmitTeam = ()=> {
    
    
    let newData = (data.map((dataElement) => {
      let newTeamName = checkboxSelectedUsers.find((element)=> {
        console.log("Girdik.!")
        console.log()
        return element.user.id == dataElement.user.id
      })
        console.log("name: ", newTeamName)
          return {...dataElement, teamName:newTeamName ? teamText : dataElement.teamName}
        }))
    updateDataWithTeam(checkboxSelectedUsers);
    setData([...newData])
    setCheckboxSelectedUsers([]);
    setTeamText("");
    setSelectedRows([]);
    setTeamButton(false);

  }
  const handleSelectClose = () =>{
    setTeamText("");
    setTeamButton(false)
  }
  const handleSelectChange = (event) => {
    console.log("Gelen event", event.target.value)
    setTeamText(event.target.value)
  }

  if (totalUsers.length <= 0) {
      return (
        <LoadingCircle/>
      );
    
  }
  if(teamButton){
    return <Dialog
    open={teamButton}
    onClose={handleSelectClose}
    aria-labelledby="alert-dialog-title"
    aria-describedby="alert-dialog-description"
  >
<FormControl sx={{ m: 1, width:300, height:25, mb:10, mr:5, ml:5 }} variant="standard">
      <InputLabel htmlFor="demo-customized-select-native">Team Name</InputLabel>
        <NativeSelect
          id="demo-customized-select-native"
          value={teamText}
          onChange={handleSelectChange}
          input={<BootstrapInput />}
        >
          {teamNames.map((name)=>{
          return <option key={name} value={name}>{name}</option>
        })}
        </NativeSelect>
    </FormControl>
    <DialogActions>
      <Button onClick={handleSelectClose}>Close</Button>
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
<Button onClick={()=> setTeamButton(true)} sx={{borderRadius:"10px"}} variant="contained" endIcon={<Diversity2Icon />}>ADD TEAM</Button>
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
            const selectedIDs = new Set(ids);
            const selectedArr = Array.from(selectedIDs)
            setSelectedRows(selectedArr);
            const selectedData = data.filter((element) => {
              return selectedArr.find((idElement) => {
                return element.user.id == idElement
              })
            }
            )
            setCheckboxSelectedUsers(selectedData);

          }}
          componentsProps={{
            filterPanel: {
              // Force usage of "And" operator
              linkOperators: [GridLinkOperator.And],
              // Display columns by ascending alphabetical order
              filterFormProps: {
                // Customize inputs by passing props
                linkOperatorInputProps: {
                  variant: 'outlined',
                  size: 'small',
                },
                columnInputProps: {
                  variant: 'outlined',
                  size: 'small',
                  sx: { mt: 'auto' },
                },
                operatorInputProps: {
                  variant: 'outlined',
                  size: 'small',
                  sx: { mt: 'auto' },
                },
                valueInputProps: {
                  InputComponentProps: {
                    variant: 'outlined',
                    size: 'small',
                  },
                },
                deleteIconProps: {
                  sx: {
                    '& .MuiSvgIcon-root': { color: '#d32f2f' },
                  },
                },
              },
              sx: {
                // Customize inputs using css selectors
                '& .MuiDataGrid-filterForm': { p: 2 },
                '& .MuiDataGrid-filterForm:nth-child(even)': {
                  backgroundColor: (theme) =>
                    theme.palette.mode === 'dark' ? '#444' : '#f5f5f5',
                },
                '& .MuiDataGrid-filterFormLinkOperatorInput': { mr: 2 },
                '& .MuiDataGrid-filterFormColumnInput': { mr: 2, width: 150 },
                '& .MuiDataGrid-filterFormOperatorInput': { mr: 2, width:150 },
                '& .MuiDataGrid-filterFormValueInput': { width: 150 },
              },
            },
          }}
          selectionModel={selectedRows}
          initialState={{
            pagination: {
              page: pageNum,
            },
            
          }}

        />
      </Box>
      </>
    
  );
};

export default AuthorPage;
