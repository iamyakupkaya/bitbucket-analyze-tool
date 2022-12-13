import Box from '@mui/material/Box';
import React, {useState, useEffect } from 'react';
import { Chart } from "react-google-charts";
import { useSelector, useDispatch } from 'react-redux';
import UserProfile from 'ui-component/user/UserProfile';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import ListItemAvatar from '@mui/material/ListItemAvatar';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import ButtonGroup from '@mui/material/ButtonGroup';
import ArrowDropDownIcon from '@mui/icons-material/ArrowDropDown';
import ClickAwayListener from '@mui/material/ClickAwayListener';
import Grow from '@mui/material/Grow';
import Paper from '@mui/material/Paper';
import Popper from '@mui/material/Popper';
import MenuItem from '@mui/material/MenuItem';
import MenuList from '@mui/material/MenuList';
import {getLastPage} from "../../../redux/pull_request/PullRequestSlice"
import LoadingCircle from 'ui-component/user/LoadingCircle';
import LoginPage from 'views/LoginPage';
import ConfirmDialog from 'ui-component/user/ConfirmDialog';
import BoltIcon from '@mui/icons-material/Bolt';
import Grid from '@mui/material/Grid';
import EarningCard from 'views/dashboard/Default/EarningCard';
import TotalIncomeDarkCard from 'views/dashboard/Default/TotalIncomeDarkCard';
import { FortTwoTone } from '@mui/icons-material';
// ==============================|| SAMPLE PAGE ||============================== //


const HomePage = () => {
    const dispatch = useDispatch();
    const totalUsers = useSelector(state => state.data.allUser)
    const totalPullRequests = useSelector(state => state.data.pullRequest)
    const activeUsers = useSelector(state => state.data.activeUser)
    const inactiveUsers = useSelector(state => state.data.inactiveUser)
    const openPR = useSelector(state => state.data.openPR);
    const mergedPR = useSelector(state => state.data.mergedPR);
    const declinedPR = useSelector(state => state.data.declinedPR)

    const userNames = useSelector(state => state.data.userNames);
    const mostReviewingUser = useSelector(state => state.data.mostReviewingUser)
    
    const [showPie, setShowPie] = useState("home")
    const [open, setOpen] = useState(false)
    const buttonOptions = useSelector(state => state.data.collections);

    const [buttonOpen, setButtonOpen] = React.useState(false);
    const anchorRef = React.useRef(null);
    const [selectedIndex, setSelectedIndex] = React.useState(0);
    const [authorText, setAuthorText] = useState("total");
    const [reposActiveUsers, setReposActiveUsers] = useState(activeUsers);
    const [reposInactiveUsers, setReposInactiveUsers] = useState(inactiveUsers)
    const [repoMostReviewingUser, setRepoMostReviewingUser] = useState(mostReviewingUser)
    
    
    
useEffect(() => {
  dispatch(getLastPage("home")) 
}, [])


    if(totalPullRequests.length <= 0 || !totalPullRequests){
      return (
        <ConfirmDialog/>
      );
      
    }

    const handleClick = () => {
      console.info(`You clicked ${options[selectedIndex]}`);
    };

    const getRepoPullRequests = (allRepos, repoName) => {
      return allRepos.filter((filteredRepo)=>{
        return filteredRepo.values.fromRef.repository.slug == repoName;
      })
    }

    const getMostReviewers=(userNameArr, allUsers)=>{
      const mostReviewers = new Map(); 
      for (let index = 0; index < userNameArr.length; index++) {
        const element = userNameArr[index]
        let count =0;
          for (let index = 0; index < allUsers.length; index++) {
            if(allUsers[index].values.reviewers.length > 0){
              allUsers[index].values.reviewers.map((reviewer)=>{
                reviewer.user.name == element ? count++ : null
                mostReviewers.set(element, count)
              })
            }  
          } 
    
      }
      const mapSort = new Map([...mostReviewers.entries()].sort((a, b) => b[1] - a[1]));
      const array = Array.from(mapSort, ([name, value]) => ({ name, value }));
      return array;
    
    }
    const handleMenuItemClick = (event, index, option) => {
      setSelectedIndex(index);
      setButtonOpen(false);
      if(option == "total"){
        setReposActiveUsers(activeUsers)
        setReposInactiveUsers(inactiveUsers)
        setAuthorText("total");
        setRepoMostReviewingUser(mostReviewingUser)
      }
      else if(option == buttonOptions[index]){
        const reposUsers = totalPullRequests.filter((filteredPull) => {
          return filteredPull.values.fromRef.repository.slug == buttonOptions[index];
        })
        
        const activeUserOnRepos = activeUsers.filter((element)=>{
          return reposUsers.find((repoUser) => {
            return repoUser.values.author.user.name == element.user.name
          })
        })
        const inactiveUserOnRepos = inactiveUsers.filter((element)=>{
          return reposUsers.find((repoUser) => {
            return repoUser.values.author.user.name == element.user.name
          })
        })
        setReposActiveUsers(activeUserOnRepos);
        setReposInactiveUsers(inactiveUserOnRepos);
        setAuthorText(buttonOptions[index]);
        
        setRepoMostReviewingUser(getMostReviewers(userNames, getRepoPullRequests(totalPullRequests, buttonOptions[index])))
      }
    };
  
    const handleToggle = () => {
      setButtonOpen((prevOpen) => !prevOpen);
    };
  
    const handleClose = (event) => {
      if (anchorRef.current && anchorRef.current.contains(event.target)) {
        return;
      }
  
      setButtonOpen(false);
    }

  


    // COLUMN CHART
    const dataColumn = [
        ["Reviewers", "Total Reviews", { role: "style" }],
        [repoMostReviewingUser[0].name, repoMostReviewingUser[0].value, "#2196f3"], // RGB value
        [repoMostReviewingUser[1].name, repoMostReviewingUser[1].value, "#2196f3"], // English color name
        [repoMostReviewingUser[2].name, repoMostReviewingUser[2].value, "#2196f3"],
        [repoMostReviewingUser[3].name, repoMostReviewingUser[3].value, "#2196f3"], // CSS-style declaration
        [repoMostReviewingUser[4].name, repoMostReviewingUser[4].value, "#2196f3"], // CSS-style declaration
        [repoMostReviewingUser[5].name, repoMostReviewingUser[5].value, "#2196f3"],
        [repoMostReviewingUser[6].name, repoMostReviewingUser[6].value, "#2196f3"],
        [repoMostReviewingUser[7].name, repoMostReviewingUser[7].value, "#2196f3"],
        [repoMostReviewingUser[8].name, repoMostReviewingUser[8].value, "#2196f3"],
        [repoMostReviewingUser[9].name, repoMostReviewingUser[9].value, "#2196f3"],
      ];

      // PIE

      const options = {
        title: `Total ${authorText == "total" ? "" : authorText} Author: ${authorText == "total" ? totalUsers.length : (reposActiveUsers.length + reposInactiveUsers.length) } `,
        is3D: true,
        titleTextStyle: {
          color: '#616161'
      },
        backgroundColor: "#e0e0e0",
        colors: ["#2196f3", "#90CAF9"],
        fontSize:18,
        chartArea:{left:0,top:30,width:"100%",height:"70%"}
    
      };
      const optionsColumn = {
        title: `Most Reviewing User for ${authorText}`,
        is3D: true,
        titleTextStyle: {
          color: '#616161'
      },
        backgroundColor: "#e0e0e0",
        colors: ["#2196f3", "#870000"],
        fontSize:20,
        chartArea:{top:50, right:20, left:100, width:"75%",height:"70%"}

    
      };
      const chartData = [
        ["General İnfo",  "Total Counts" ],
        ["Active", reposActiveUsers.length],
        ["Inactive", reposInactiveUsers.length],
      ];

      if(showPie =="slice#0" && open){
        return <UserProfile data={{open, setOpen, arr:reposActiveUsers}}/>
      } 
      else if(showPie =="slice#1" && open){
        return <UserProfile data={{open, setOpen, arr:reposInactiveUsers }}/>

      }
      else if(showPie =="bar#0#0" && open){
        const myarr = totalUsers.filter((author)=> {
          return author.user.name == repoMostReviewingUser[0].name
        })
        return <UserProfile data={{open, setOpen, arr:myarr }}/>

      }
      else if(showPie =="bar#0#1" && open){
        const myarr = totalUsers.filter((author)=> {
          return author.user.name == repoMostReviewingUser[1].name
        })
        return <UserProfile data={{open, setOpen, arr:myarr }}/>

      }
      else if(showPie =="bar#0#2" && open){
        const myarr = totalUsers.filter((author)=> {
          return author.user.name == repoMostReviewingUser[2].name
        })
        return <UserProfile data={{open, setOpen, arr:myarr }}/>

      }
      else if(showPie =="bar#0#3" && open){
        const myarr = totalUsers.filter((author)=> {
          return author.user.name == repoMostReviewingUser[3].name
        })
        return <UserProfile data={{open, setOpen, arr:myarr }}/>

      }
      else if(showPie =="bar#0#4" && open){
        const myarr = totalUsers.filter((author)=> {
          return author.user.name == repoMostReviewingUser[4].name
        })
        return <UserProfile data={{open, setOpen, arr:myarr }}/>

      }
      else if(showPie =="bar#0#5" && open){
        const myarr = totalUsers.filter((author)=> {
          return author.user.name == repoMostReviewingUser[5].name
        })
        return <UserProfile data={{open, setOpen, arr:myarr }}/>

      }
      else if(showPie =="bar#0#6" && open){
        const myarr = totalUsers.filter((author)=> {
          return author.user.name == repoMostReviewingUser[6].name
        })
        return <UserProfile data={{open, setOpen, arr:myarr }}/>

      }
      else if(showPie =="bar#0#7" && open){
        const myarr = totalUsers.filter((author)=> {
          return author.user.name == repoMostReviewingUser[7].name
        })
        return <UserProfile data={{open, setOpen, arr:myarr }}/>

      }
      else if(showPie =="bar#0#8" && open){
        const myarr = totalUsers.filter((author)=> {
          return author.user.name == repoMostReviewingUser[8].name
        })
        return <UserProfile data={{open, setOpen, arr:myarr }}/>

      }
      else if(showPie =="bar#0#9" && open){
        const myarr = totalUsers.filter((author)=> {
          return author.user.name == repoMostReviewingUser[9].name
        })
        return <UserProfile data={{open, setOpen, arr:myarr }}/>

      }
      
 
    return (
    
  <>
  <ButtonGroup sx={{mb:0}} variant="contained" ref={anchorRef} aria-label="split button">
        <Button onClick={handleClick}>{buttonOptions[selectedIndex]}</Button>
        <Button
        
          size="small"
          aria-controls={buttonOpen ? 'split-button-menu' : undefined}
          aria-expanded={buttonOpen ? 'true' : undefined}
          aria-label="select merge strategy"
          aria-haspopup="menu"
          onClick={handleToggle}
        >
          <ArrowDropDownIcon />
        </Button>
      </ButtonGroup>
      <Popper
        sx={{
          zIndex: 1,
        }}
        open={buttonOpen}
        anchorEl={anchorRef.current}
        role={undefined}
        transition
        disablePortal
      >
        {({ TransitionProps, placement }) => (
          <Grow
            {...TransitionProps}
            style={{
              transformOrigin:
                placement === 'bottom' ? 'center top' : 'center bottom',
            }}
          >
            <Paper>
              <ClickAwayListener onClickAway={handleClose}>
                <MenuList id="split-button-menu" autoFocusItem>
                  {buttonOptions.map((option, index) => (
                    <MenuItem
                      key={option}
                      selected={index === selectedIndex}
                      onClick={(event) => handleMenuItemClick(event, index, option)}
                    >
                      {option}
                    </MenuItem>
                  ))}
                </MenuList>
              </ClickAwayListener>
            </Paper>
          </Grow>
        )}
      </Popper>
       
       <Box sx={{width:"100%", display:"flex"}}>
       <Grid container spacing={2} sx={{mt:4, mb:0}}>
      <Grid item xs={6} md={6}>
        <EarningCard data={{isLoading:false,  name:"Active Users", users:reposActiveUsers}} />

      </Grid>
      <Grid item xs={6} md={6}>
<EarningCard data={{isLoading:false, name:"Inactive Users", users:reposInactiveUsers}} />
      </Grid> 
</Grid>
                    


       </Box>


<Grid container spacing={2} sx={{mt:1, mb:5}}>
      <Grid item xs={6} md={6}>
        <TotalIncomeDarkCard data={{isLoading:false, count: (authorText == "total" ? totalPullRequests.length : (getRepoPullRequests(totalPullRequests, authorText)).length), name:(`${authorText} Pull Requests`)}} />
      </Grid>
      <Grid item xs={6} md={6}>
      <TotalIncomeDarkCard data={{isLoading:false, count: (authorText == "total" ? mergedPR.length : (getRepoPullRequests(mergedPR, authorText)).length), name:(`${authorText} Merged Pull Requests`)}} />

      </Grid>
      <Grid item xs={6} md={6}>
      <TotalIncomeDarkCard data={{isLoading:false, count: (authorText == "total" ? declinedPR.length : (getRepoPullRequests(declinedPR, authorText)).length), name:(`${authorText} Declined Pull Requests`)}} />
      </Grid>
      <Grid item xs={6} md={6}>
      <TotalIncomeDarkCard data={{isLoading:false, count: (authorText == "total" ? openPR.length : (getRepoPullRequests(openPR, authorText)).length), name:(`${authorText} Open Pull Requests`)}} />

      </Grid> 
</Grid>


<Chart chartType="ColumnChart"            options={optionsColumn}
 width="100%" height="500px" data={dataColumn}  chartEvents={[
  {
    eventName: "ready",
    callback: ({ chartWrapper, google }) => {
      const chart = chartWrapper.getChart();
      google.visualization.events.addListener(chart, "click", e => {
        setOpen(true)
      });
      google.visualization.events.addListener(chart, "click", e => {

          setShowPie(e.targetID)
          setOpen(true)

      });
    }
  }
]} />

    </>
       
)
};

export default HomePage;





/*


       <Chart
            chartType="PieChart"
            data={chartData}
            options={options}
            width={"100%"}
            height={"400px"}
            left={0}
            
            chartEvents={[
                {
                  eventName: "ready",
                  callback: ({ chartWrapper, google }) => {
                    const chart = chartWrapper.getChart();
                    google.visualization.events.addListener(chart, "click", e => {
                    
                      setShowPie(e.targetID) // slice#0  
                      setOpen(true)
                    });
                    google.visualization.events.addListener(chart, "click", e => {
                        setShowPie(e.targetID)
                        setOpen(true)

                    });
                  }
                }
              ]}
            
            />

*/