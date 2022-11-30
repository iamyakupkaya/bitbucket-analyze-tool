
import Box from '@mui/material/Box';
import {useState } from 'react';
import { Chart } from "react-google-charts";
import { useSelector, useDispatch } from 'react-redux';
import UserProfile from 'ui-component/user/UserProfile';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import ListItemAvatar from '@mui/material/ListItemAvatar';
import Avatar from '@mui/material/Avatar';
import ImageIcon from '@mui/icons-material/Image';
import WorkIcon from '@mui/icons-material/Work';
import BeachAccessIcon from '@mui/icons-material/BeachAccess';
import CenterFocusStrongIcon from '@mui/icons-material/CenterFocusStrong';
// ==============================|| SAMPLE PAGE ||============================== //


const HomePage = () => {
    const dispatch = useDispatch();
    const totalUsers = useSelector(state => state.data.allUser)
    const totalPullRequests = useSelector(state => state.data.pullRequest)
    const activeUsers = useSelector(state => state.data.activeUser)
    const inactiveUsers = useSelector(state => state.data.inactiveUser)
    const openPR = useSelector(state => state.data.openPR)
    const mergedPR = useSelector(state => state.data.mergedPR)
    const declinedPR = useSelector(state => state.data.declinedPR)
    const mostReviewingUser = useSelector(state => state.data.mostReviewingUser)
console.log("Bak burada", mostReviewingUser)



    const [showPie, setShowPie] = useState("home")
    const [open, setOpen] = useState(false)
    // COLUMN CHART
    const dataColumn = [
        ["Reviewers", "Total Reviews", { role: "style" }],
        [mostReviewingUser[0].name, mostReviewingUser[0].value, "#2196f3"], // RGB value
        [mostReviewingUser[1].name, mostReviewingUser[1].value, "#2196f3"], // English color name
        [mostReviewingUser[2].name, mostReviewingUser[2].value, "#2196f3"],
        [mostReviewingUser[3].name, mostReviewingUser[3].value, "#2196f3"], // CSS-style declaration
        [mostReviewingUser[4].name, mostReviewingUser[4].value, "#2196f3"], // CSS-style declaration

      ];
  

      // PIE

      const options = {
        title: `Total Author: ${totalUsers.length}`,
        is3D: true,
        backgroundColor: "#e3f2fd",
        colors: ["#2196f3", "#5e35b1"],
        fontSize:20,
    
      };
      const optionsColumn = {
        title: `Most Reviewing User`,
        is3D: true,
        backgroundColor: "#e3f2fd",
        colors: ["#006100", "#870000"],
        fontSize:20,
    
      };
      const chartData = [
        ["General İnfo",  "Total Counts" ],
        ["Active", activeUsers.length],
        ["Inactive", inactiveUsers.length],
      ];

      if(showPie =="slice#0" && open){
        return <UserProfile data={{open, setOpen, arr:activeUsers}}/>
      } 
      else if(showPie =="slice#1" && open){
        return <UserProfile data={{open, setOpen, arr:inactiveUsers }}/>

      }
      else if(showPie =="bar#0#0" && open){
        const myarr = totalUsers.filter((author)=> {
          return author.user.name == mostReviewingUser[0].name
        })
        console.log("Al sana my arr", myarr)
        return <UserProfile data={{open, setOpen, arr:myarr }}/>

      }
      else if(showPie =="bar#0#1" && open){
        const myarr = totalUsers.filter((author)=> {
          return author.user.name == mostReviewingUser[1].name
        })
        console.log("Al sana my arr", myarr)
        return <UserProfile data={{open, setOpen, arr:myarr }}/>

      }
      else if(showPie =="bar#0#2" && open){
        const myarr = totalUsers.filter((author)=> {
          return author.user.name == mostReviewingUser[2].name
        })
        console.log("Al sana my arr", myarr)
        return <UserProfile data={{open, setOpen, arr:myarr }}/>

      }
      else if(showPie =="bar#0#3" && open){
        const myarr = totalUsers.filter((author)=> {
          return author.user.name == mostReviewingUser[3].name
        })
        console.log("Al sana my arr", myarr)
        return <UserProfile data={{open, setOpen, arr:myarr }}/>

      }
      else if(showPie =="bar#0#4" && open){
        const myarr = totalUsers.filter((author)=> {
          return author.user.name == mostReviewingUser[4].name
        })
        console.log("Al sana my arr", myarr)
        return <UserProfile data={{open, setOpen, arr:myarr }}/>

      }
 
    return (
    <>
        <Chart
            chartType="PieChart"
            data={chartData}
            options={options}
            width={"100%"}
            height={"400px"}
            chartEvents={[
                {
                  eventName: "ready",
                  callback: ({ chartWrapper, google }) => {
                    const chart = chartWrapper.getChart();
                    console.log("chartwrapper ", chartWrapper)
                    google.visualization.events.addListener(chart, "click", e => {
                    
                    console.log("gelen chart", chart)
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
     <Chart chartType="ColumnChart"            options={optionsColumn}
 width="100%" height="500px" data={dataColumn}  chartEvents={[
  {
    eventName: "ready",
    callback: ({ chartWrapper, google }) => {
      const chart = chartWrapper.getChart();
      google.visualization.events.addListener(chart, "click", e => {
        console.log("Gelen targetid:", e.targetID)
        setOpen(true)
      });
      google.visualization.events.addListener(chart, "click", e => {
        console.log("Tıklandı")

          setShowPie(e.targetID)
          setOpen(true)

      });
    }
  }
]} />


              <Box sx={{display:"flex", justifyContent:"space-between"}}>
    <List sx={{ width: '100%', maxWidth: 360, bgcolor: '#e3f2fd' }}>
      <ListItem>
        <ListItemAvatar>
          <Avatar>
            <CenterFocusStrongIcon />
          </Avatar>
        </ListItemAvatar>
        <ListItemText primaryTypographyProps={{fontSize: '20px', fontWeight:"bold"}} secondaryTypographyProps={{fontSize: '15px', fontWeight:"bold"}} primary="Total Pull Request" secondary={totalPullRequests.length} />
      </ListItem>
      <ListItem>
        <ListItemAvatar>
          <Avatar>
            <CenterFocusStrongIcon />
          </Avatar>
        </ListItemAvatar>
        <ListItemText primaryTypographyProps={{fontSize: '20px', fontWeight:"bold"}} secondaryTypographyProps={{fontSize: '15px', fontWeight:"bold"}} primary="Open Pull Request Count" secondary={openPR.length} />
      </ListItem>
      <ListItem>
        <ListItemAvatar>
          <Avatar>
            <CenterFocusStrongIcon />
          </Avatar>
        </ListItemAvatar>
        <ListItemText primaryTypographyProps={{fontSize: '20px', fontWeight:"bold"}} secondaryTypographyProps={{fontSize: '15px', fontWeight:"bold"}} primary="Declined Pull Request Count" secondary={declinedPR.length} />
      </ListItem>
      <ListItem>
        <ListItemAvatar>
          <Avatar>
            <CenterFocusStrongIcon />
          </Avatar>
        </ListItemAvatar>
        <ListItemText primaryTypographyProps={{fontSize: '20px', fontWeight:"bold"}} secondaryTypographyProps={{fontSize: '15px', fontWeight:"bold"}} primary="Merged Pull Request Count" secondary={mergedPR.length} />
      </ListItem>
    </List>
              </Box>
    </>
       
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