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
import CancelIcon from '@mui/icons-material/Cancel';
import Button from '@mui/material/Button';
import ReviewerCard from './ReviewerCard';
import Grid from '@mui/material/Grid';
import Divider from '@mui/material/Divider';
import Chip from '@mui/material/Chip';




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


export default function FullScreenDialog(props) {
  const {open, setOpen, selectedPR } = props.data;
  const handleClose = () => {
    setOpen(false)
  };

  return (
    <div>
      <Dialog
        fullScreen
        open={open}
        onClose={handleClose}
        TransitionComponent={Transition}
      >
        <AppBar sx={{ position: 'relative', backgroundColor:"#e3f2fd" }}>
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
            
          <Button
          onClick={handleClose}
          variant="contained" sx={{backgroundColor:"#2196f3"}} size="small" startIcon={<CancelIcon color="error" />}>
          <Typography variant="h5" sx={{fontWeight:"bold", color:"white"}}>
              Close
</Typography>
      </Button>
          </Toolbar>
        </AppBar>
        <Box sx={{flexGrow:1}}>
          <Box sx={{mt:1.5}} >
          <Divider sx={{mb:5}}>
            <Chip sx={{ backgroundColor:"#2196f3", color:"white", fontWeight:"bold"}} label={"STATE: " + selectedPR.values.state} />
            <Chip sx={{marginRight:"15vw", marginLeft:"15vw",  backgroundColor:"#2196f3", color:"white", fontWeight:"bold"}} label={"PROJECT: "+selectedPR.values.fromRef.repository.project.name} />
            <Chip sx={{backgroundColor:"#2196f3", color:"white", fontWeight:"bold"}} label={"REPOSITORY: "+selectedPR.values.fromRef.repository.slug} />
          </Divider>
          <Divider sx={{mb:5}}>
            <Chip sx={{marginRight:"15vw", backgroundColor:"#2196f3", color:"white", fontWeight:"bold"}} label={"CREATED: " + new Date(selectedPR.values.createdDate).toISOString().split("T")[0]} />
            <Chip sx={{backgroundColor:"#2196f3", color:"white", fontWeight:"bold"}} label={"UPDATED: "+new Date(selectedPR.values.updatedDate).toISOString().split("T")[0]} />
          </Divider>
          <Divider textAlign="left" sx={{fontWeight:"bold"}}>TITLE</Divider>
          <Box sx={{ marginRight:"10px", marginLeft:"10px", mt:1}} spacing={5}>
            {selectedPR.values.title}
          </Box>
          <Divider textAlign="left" sx={{fontWeight:"bold", mt:2}}>DESCRIPTION</Divider>
          <Box sx={{ marginRight:"10px", marginLeft:"10px", mt:1}} spacing={5}>
            {selectedPR.values.description}
          </Box>
          <Divider textAlign="left" sx={{fontWeight:"bold", mt:2}}>DESCRIPTION</Divider>
          <Box sx={{ marginRight:"10px", marginLeft:"10px", mt:1}} spacing={5}>
            {selectedPR.values.description}
          </Box>
          </Box>
        <Grid container spacing={5}>
        <Grid item xs={6} md={6} lg={4}>
        <ReviewerCard/>

        </Grid>
        <Grid item xs={6} md={6} lg={4}>
        <ReviewerCard/>
        </Grid>
        <Grid item xs={6} md={6} lg={4}>
        <ReviewerCard/>
        </Grid>
        
      </Grid>
         

          <Divider />
         
        </Box>
      </Dialog>
    </div>
  );
}

/* FullScreenDialog.defaultProps = {
  data:{
    selectedPR:{
      values:{
        author:{
          user:{
            displayName:"Unknown",
            emailAddress:"Unknown"
          }
        }
      }
    }
  }
};

*/
