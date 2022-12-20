import React, {useState} from 'react';
import Dialog from '@mui/material/Dialog';
import Avatar from '@mui/material/Avatar';
import Stack from '@mui/material/Stack';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Slide from '@mui/material/Slide';
import Badge from '@mui/material/Badge';
import Box from '@mui/material/Box';
import { styled } from '@mui/material/styles';
import IconButton from '@mui/material/IconButton';
import Divider from '@mui/material/Divider';
import Chip from '@mui/material/Chip';
import { DataGrid, GridToolbar } from "@mui/x-data-grid";
import CancelPresentationIcon from '@mui/icons-material/CancelPresentation';
import Button from '@mui/material/Button';
import InfoIcon from "@mui/icons-material/Info";
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import Paper from '@mui/material/Paper';
import Draggable from 'react-draggable';
import Typography from '@mui/material/Typography';
import ReactMarkdown from 'react-markdown'
import Link from '@mui/material/Link';

const Transition = React.forwardRef(function Transition(props, ref) {
  return <Slide direction="up" ref={ref} {...props} />;
});


const StyledBadge = styled(Badge)((props) => ({
  
  '& .MuiBadge-badge': {
    backgroundColor: `${props.active == true ? "#44b700" : "red" }`,
    color: `${props.active == true ? "#44b700" : "red" }`,
    boxShadow: `0 0 0 2px ${props.theme.palette.background.paper}`,
    '&::after': {
      position: 'absolute',
      top: 0,
      left: 0,
      width: '100%',
      height: '100%',
      borderRadius: '50%',
      animation: 'ripple 1.2s infinite ease-in-out',
      border: '1px solid currentColor',
      content: '""',
    },
  },
  '@keyframes ripple': {
    '0%': {
      transform: 'scale(.8)',
      opacity: 1,
    },
    '100%': {
      transform: 'scale(2.4)',
      opacity: 0,
    },
  },
}));

function stringToColor(string="Unknown Unknown") {

  let hash = 0;
  let i;

  /* eslint-disable no-bitwise */
  for (i = 0; i < string.length; i += 1) {
    hash = string.charCodeAt(i) + ((hash << 5) - hash);
  }

  let color = '#';

  for (i = 0; i < 3; i += 1) {
    const value = (hash >> (i * 8)) & 0xff;
    color += `00${value.toString(16)}`.slice(-2);
  }
  /* eslint-enable no-bitwise */

  return color;
}

function stringAvatar(name = "Unknown Unknown") {
  name = name.includes(" ") ? name : name+" ?";
  return {
    sx: {
      bgcolor: stringToColor(name),
    },
    children: `${name.split(' ')[0][0]}${name.split(' ')[1][0]}`,
  };
}

function createData(
    pr,
    id,
    state,
    slug,
    createdDate,
    updatedDate,
    title,
    description,
  ) {
    return { pr, id, state, slug, createdDate, updatedDate, title, description };
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

export default function PullRequestList(props) {
  const {open, setOpen, selectedPR } = props.data;
  const [pageSize, setPageSize] = useState(10);
  const [pageNum, setPageNum] = React.useState(0);
  const [currentData, setCurrentData] = useState({});
const [showInfo, setShowInfo] = useState(false)
  const handleClose = () => {
    setOpen(false)
  };
  const handleCloseInfo = (reason) => {
    reason == "backdropClick" ? setShowInfo(true) : setShowInfo(false)
  };
  


  const rows = selectedPR.map((pr) => {
    return createData(
      pr,
      pr.id,
      pr.values.state,
      pr.values.fromRef.repository.slug,
      new Date(pr.values.createdDate).toISOString().split("T")[0],
      new Date(pr.values.updatedDate).toISOString().split("T")[0],
      pr.values.title,
      pr.values.description,


    );
  });

  const columns = [
    {
        field: "info",
        headerName: "INFO",
        filterable: false,
        disableClickEventBubbling: true,
        sortable: false,
  
        renderCell: (params) => {
          const onClick = (e) => {
            //setSelectedPR({ ...params.row.pr });
            setCurrentData(params.row.pr);
            setShowInfo(true)
          };
  
          return (
            <Stack direction="row" spacing={2}>
              <IconButton onClick={onClick} sx={{ borderRadius: 25, color: "#21a2f6" }} aria-label="info">
  <InfoIcon />
</IconButton>
            </Stack>
          );
        },
        flex: 0.3,
      },
    {
      field: "state",
      headerName: "State",
      flex: 0.5,
    },
    {
        field: "slug",
        headerName: "Repository",
        flex: 0.5,
      },
    {
        field: "createdDate",
        headerName: "Created Date",
        flex: 0.5,
    },
    {
      field: "updatedDate",
      headerName: "Updated Date",
      flex: 0.5,
    },
    {
      field: "title",
      headerName: "Title",
      flex: 1,
    },
    {
        field: "description",
        headerName: "Description",
        flex: 2,
      },
    

 
  ];

  function getJiraID(str="Unknown") {
    let result = []
    let pattern = /[A-Z]+-+[0-9]+/g;
    result = str.match(pattern);
    if(result){
      const newResult = Array.from(new Set(result));
      return newResult;
    }
      return [];
    
  }

  if (showInfo) {
    return (
        <Dialog
        open={open}
        onClose={(event, reason)=> handleCloseInfo(reason)}
        PaperComponent={PaperComponent}
        aria-labelledby="draggable-dialog-title"
        maxWidth={"xl"}
      >
        <DialogTitle style={{ cursor: 'move', fontWeight:"bold", fontSize:"20px" }} id="draggable-dialog-title">
        <ReactMarkdown>{currentData.values.title || "Unknown"}</ReactMarkdown>
        <ReactMarkdown>Links:</ReactMarkdown>
          {getJiraID(currentData.values.title).length <=0
          ? <ReactMarkdown>There is no Link</ReactMarkdown>
          :
          getJiraID(currentData.values.title).map((ID) => {
            return <Box key={ID}>
              <Link target="_blank"  href={"https://jira.rbbn.com/browse/"+ID} underline="hover">
            {"https://jira.rbbn.com/browse/"+ID}
            </Link>
            </Box>
          })}
        </DialogTitle>
       
        <Box sx={{display:"flex", flexDirection:"column"}}>
        <DialogContent sx={{wordWrap: "break-word"}}>
        <ReactMarkdown>Links:</ReactMarkdown>
          {getJiraID(currentData.values.description).length <=0
          ? <ReactMarkdown>There is no Link</ReactMarkdown>
          :
          getJiraID(currentData.values.description).map((ID) => {
            return <Box key={ID}>
              <Link target="_blank"  href={"https://jira.rbbn.com/browse/"+ID} underline="hover">
            {"https://jira.rbbn.com/browse/"+ID}
            </Link>
            </Box>
          })}
        <ReactMarkdown>{currentData.values.description || "Unknown"}</ReactMarkdown>
        </DialogContent>
        <DialogContent sx={{wordWrap: "break-word"}}>
        <Divider sx={{mt:2, mb:2}}>
            <Chip sx={{ backgroundColor:"#2196f3", color:"white", fontWeight:"bold"}} label={"Reviewers of this Pull Request"} />
          </Divider>
        {currentData.values.reviewers.map((mappingReviewer) => {
            return (<DialogContent key={mappingReviewer.user.id} sx={{wordWrap: "break-word"}}> 
                      <Typography component={'p'} variant={'body2'} style={{ cursor: 'move', fontSize:"15px", color:"grey", fontWeight:"normal",  }}>{mappingReviewer.user.displayName}</Typography>
                      <Typography component={'p'} variant={'body2'} style={{ cursor: 'move', fontSize:"15px", color:"grey", fontWeight:"normal",  }}>{mappingReviewer.user.emailAddress}</Typography>
                      <Typography component={'p'} variant={'body2'} style={{ cursor: 'move', fontSize:"15px", color:"grey", fontWeight:"normal",  }}>{mappingReviewer.user.name}</Typography>

            </DialogContent>)
            
        })}
        </DialogContent>
        </Box>
        <DialogActions style={{display:"flex", justifyContent:"flex-start", alignContent:"center"}}>
          <Button onClick={handleCloseInfo}>
            Close
          </Button>
        </DialogActions>
      </Dialog>
    );
  }


  return (
    <div>
      <Dialog
        fullScreen
        open={open}
        onClose={handleClose}
        TransitionComponent={Transition}
      >
        <AppBar sx={{ position: 'relative', backgroundColor:"#e0e0e0" }}>
          <Toolbar sx={{ display:"flex", justifyContent:"space-between"}}>
          <Stack direction="row" spacing={2}>
          <StyledBadge
  overlap="circular"
  anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
  variant="dot"
  active={selectedPR[0].values.author.user.active}
>
<Avatar {...stringAvatar(selectedPR[0].values.author.user.displayName)} />
</StyledBadge>
          <Typography sx={{ ml: -1, flex: 1 }} variant="h5" component="div">
            {selectedPR[0].values.author.user.displayName}
            <br/>
            {selectedPR[0].values.author.user.emailAddress}
            </Typography>
          </Stack>
            
          <IconButton aria-label="delete" color="error" size="medium" onClick={handleClose}>
  <CancelPresentationIcon  fontSize="inherit" />
</IconButton>
          </Toolbar>
        </AppBar>
        <Box sx={{flexGrow:1}}>
        <Divider sx={{mt:3}}>
            <Chip sx={{ backgroundColor:"#2196f3", color:"white", fontWeight:"bold"}} label={"Pull Requests of " + selectedPR[0].values.author.user.name} />
          </Divider>

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
        </Box>
      </Dialog>
      

    </div>
  );
}

/* <ReviewerList data={{ open: open, setOpen: setOpen, selectedPR: userReviewer }}/> */
