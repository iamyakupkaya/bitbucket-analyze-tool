import { createSlice } from "@reduxjs/toolkit";


const splitAuthors=(arr)=>{
  const mySet1 = new Set(); 
  for (let index = 0; index < arr.length; index++) {
    mySet1.add(arr[index].values.author.user.name)
    for(let j =0; j< arr[index].values.reviewers.length; j++){
      mySet1.add(arr[index].values.reviewers[j].user.name)

    }
    
  }
  
  const userNames = [...mySet1]
  console.log("userNames: ",userNames)
  return userNames;
}

const getAllUsers=(userNameArr, allUsers)=>{
  let userAuthor = allUsers.map((element) =>{
    return element.values.author
  })
  let userRevi = [].concat(...allUsers.map(ap => ap.values.reviewers))
  const allUserInfo = userRevi.concat(userAuthor)
  const newArr = userNameArr.map((name)=> {
    return allUserInfo.find((element)=> {
        return element.user.name === name;
    })
  })
  console.log("new arr ",newArr)
  return newArr;
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

const getCollections=(pullRequests)=>{
  const repoNames = pullRequests.map((pull)=> {
    return pull.values.fromRef.repository.slug;
  })
  const collections = [...(new Set(repoNames))]
  collections.unshift("total");
  return collections;

}

const getActiveUsers = (allusers) => {
    return allusers.filter((filteredUser)=>{
      return filteredUser.user.active == true
    })
}


const getOpenPR = (pullRequests) => {
  return pullRequests.filter((element) => {
    return element.values.state === "OPEN"
  })
}

const getMergedPR = (pullRequests) => {
  return pullRequests.filter((element) => {
    return element.values.state === "MERGED"
  })
}

const getDeclinedPR = (pullRequests) => {
  return pullRequests.filter((element) => {
    return element.values.state === "DECLINED"
  })
}

const getInactiveUsers = (allusers) => {
  return allusers.filter((filteredUser)=>{
    return filteredUser.user.active == false
  })
}

const getUserPullRequest = (pullRequest, users)=>{
  const arr = users.map((pr) => {
    return pullRequest.filter((filteredPull) => {
      return filteredPull.values.author.user.name == pr.user.name
  })
  })
  return arr;
}

const getUserReviewing = (pullRequest, users)=>{
  const arr = users.map((pr) => {
    return pullRequest.filter((element)=>{
      return (element.values.reviewers.find((insideMap) => {
         return insideMap.user.name == pr.user.name
      }))
 })
  })
  return arr;
}


const initialState = {
  pullRequest:[],
  activeUser:[],
  inactiveUser:[],
  userNames:[],
  allUser:[],
  openPR:[],
  mergedPR:[],
  declinedPR:[],
  mostReviewingUser:[],
  collections:[],
  activeUserPullRequest:[],
  inactiveUserPullRequest:[],
  activeUserReviewing:[],
  inactiveUserReviewing:[],
  reviewersName:[],
  reviewerUsers:[],
  lastPage:"home",
  loadingText:"Please wait, data is loading..."
};



const PullRequestSlice = createSlice({
  name: "open", // this name using in action type
  initialState, //this is initial state
  reducers: {
    getPullRequests: (state, action) => {
      console.log("gelen data: ", action.payload);
      state.pullRequest = action.payload;
      state.userNames = splitAuthors(action.payload);
      state.allUser = getAllUsers(state.userNames, action.payload)
      state.activeUser = getActiveUsers(state.allUser)
      state.inactiveUser = getInactiveUsers(state.allUser)
      state.openPR = getOpenPR(state.pullRequest)
      state.mergedPR = getMergedPR(state.pullRequest)
      state.declinedPR = getDeclinedPR(state.pullRequest)
      state.mostReviewingUser = getMostReviewers(state.userNames, state.pullRequest)
      state.collections = getCollections(state.pullRequest);
      state.activeUserPullRequest = getUserPullRequest(action.payload, state.activeUser)
      state.inactiveUserPullRequest = getUserPullRequest(action.payload, state.inactiveUser)
      state.activeUserReviewing = getUserReviewing(action.payload, state.activeUser)
      state.inactiveUserReviewing = getUserReviewing(action.payload, state.inactiveUser)

    },
    getLastPage:(state, action) =>{
      state.lastPage = action.payload;
    },
    setLoadingText:(state, action) => {
      state.loadingText = action.payload;
    }
  },
});

export const PullRequestReducer =  PullRequestSlice.reducer;
export const { getPullRequests, getLastPage, getReviewers, setLoadingText } = PullRequestSlice.actions;