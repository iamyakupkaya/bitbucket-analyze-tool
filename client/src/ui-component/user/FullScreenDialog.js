import * as React from 'react';
import Dialog from '@mui/material/Dialog';
import Avatar from '@mui/material/Avatar';
import Stack from '@mui/material/Stack';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Slide from '@mui/material/Slide';
import Badge from '@mui/material/Badge';
import Box from '@mui/material/Box';
import { styled } from '@mui/material/styles';
import IconButton from '@mui/material/IconButton';
import ReviewerCard from './ReviewerCard';
import Grid from '@mui/material/Grid';
import Divider from '@mui/material/Divider';
import Chip from '@mui/material/Chip';
import CancelPresentationIcon from '@mui/icons-material/CancelPresentation';
import ReactMarkdown from 'react-markdown'
import Link from '@mui/material/Link';




const Transition = React.forwardRef(function Transition(props, ref) {
  return <Slide direction="up" ref={ref} {...props} />;
});


const StyledBadge = styled(Badge)((props) => ({
  
  '& .MuiBadge-badge': {
    backgroundColor: `${props.active ? "#44b700" : "red" }`,
    color: `${props.active ? "#44b700" : "red" }`,
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

export default function FullScreenDialog(props) {
  const {open, setOpen, selectedPR } = props.data;
  const handleClose = () => {
    setOpen(false)
  };
  console.log("SELECTED", selectedPR)

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
  active={selectedPR.values.author.user.active}
>
<Avatar {...stringAvatar(selectedPR.values.author.user.displayName)} />
</StyledBadge>
          <Typography sx={{ ml: -1, flex: 1 }} variant="h5" component="div">
            {selectedPR.values.author.user.displayName}
            <br/>
            {selectedPR.values.author.user.emailAddress}
            </Typography>
          </Stack>
            
          <IconButton aria-label="delete" color="error" size="medium" onClick={handleClose}>
  <CancelPresentationIcon  fontSize="inherit" />
</IconButton>
          </Toolbar>
        </AppBar>
        <Box sx={{flexGrow:1}}>
          <Box sx={{mt:1.5}} >
          <Divider sx={{mb:5}}>
            <Chip sx={{ backgroundColor:"#2196f3", color:"white", fontWeight:"bold"}} label={"STATE: " + selectedPR.values.state} />
            <Chip sx={{marginRight:"10vw", marginLeft:"10vw",  backgroundColor:"#2196f3", color:"white", fontWeight:"bold"}} label={"PROJECT: "+selectedPR.values.fromRef.repository.project.name} />
            <Chip sx={{backgroundColor:"#2196f3", color:"white", fontWeight:"bold"}} label={"REPOSITORY: "+selectedPR.values.fromRef.repository.slug} />
          </Divider>
          <Divider sx={{mb:5}}>
            <Chip sx={{marginRight:"10vw", backgroundColor:"#2196f3", color:"white", fontWeight:"bold"}} label={"CREATED: " + new Date(selectedPR.values.createdDate).toISOString().split("T")[0]} />
            <Chip sx={{backgroundColor:"#2196f3", color:"white", fontWeight:"bold"}} label={"UPDATED: "+new Date(selectedPR.values.updatedDate).toISOString().split("T")[0]} />
          </Divider>
          <Divider textAlign="left" sx={{fontWeight:"bold"}}>TITLE</Divider>
          <Box sx={{ marginRight:"10px", marginLeft:"10px", mt:1}} spacing={5}>
          <ReactMarkdown>{selectedPR.values.title}</ReactMarkdown>
          <ReactMarkdown>Links:</ReactMarkdown>
          {getJiraID(selectedPR.values.title).length <=0
          ? <ReactMarkdown>There is no Link</ReactMarkdown>
          :
          getJiraID(selectedPR.values.title).map((ID) => {
            return <Box key={ID}>
              <Link target="_blank"  href={"https://jira.rbbn.com/rest/agile/1.0/issue/"+ID} underline="hover">
            {"https://jira.rbbn.com/rest/agile/1.0/issue/"+ID}
            </Link>
            </Box>
          })}
          
          </Box>
          <Divider textAlign="left" sx={{fontWeight:"bold", mt:2}}>DESCRIPTION</Divider>
          <Box sx={{ marginRight:"10px", marginLeft:"10px", mt:1}} spacing={5}>
          <ReactMarkdown>Links:</ReactMarkdown>
          {getJiraID(selectedPR.values.description).length <=0
          ? <ReactMarkdown>There is no Link</ReactMarkdown>
          :
          getJiraID(selectedPR.values.description).map((ID) => {
            return <Box key={ID}>
              <Link target="_blank"  href={"https://jira.rbbn.com/rest/agile/1.0/issue/"+ID} underline="hover">
            {"https://jira.rbbn.com/rest/agile/1.0/issue/"+ID}
            </Link>
            </Box>
          })}
          <ReactMarkdown>{selectedPR.values.description}</ReactMarkdown>
          </Box>
          <Divider textAlign="left" sx={{fontWeight:"bold", mt:2, mb:2}}>REVIEWERS</Divider>
          </Box>
        <Grid container spacing={3}>
        {selectedPR.values.reviewers.map((reviewer, index, arr) => {
          if(arr.length<3){
            return(
              <Grid key={reviewer.user.id} sx={{display:"flex", justifyContent:"center", alignItems:"center"}} item xs={12} sm={6} md={6} lg={4} xl={3}>
              <ReviewerCard  data={reviewer}/>
              <br></br>
              </Grid> 
  
            )
          }
          return(
            <Grid key={reviewer.user.id} sx={{display:"flex", justifyContent:"center", alignItems:"center"}} item xs={12} sm={6} md={6} lg={4} xl={3}>
            <ReviewerCard data={reviewer}/>
            <br></br>

            </Grid> 

          )
        })}
      </Grid>
                  
        </Box>
      </Dialog>
    </div>
  );
}





