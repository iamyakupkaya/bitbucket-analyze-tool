import { createSlice } from "@reduxjs/toolkit";


const splitUsers=(arr)=>{
  const mySet1 = new Set(); 
  for (let index = 0; index < arr.length; index++) {
    mySet1.add(arr[index].values.author.user.name)
    
  }
  const userNames = [...mySet1]
  return userNames;
}

const getAllUsers=(userNameArr, allUsers)=>{
  const user = allUsers.map((element) =>{
    return element.values.author.user
  })
  const newArr = userNameArr.map((name)=> {
    return user.find((element)=> {
        return element.name === name;
    })
  })

  return newArr;
}

const initialState = {
  pullRequest:[],
  activeUser:[],
  userNames:[],
  allUser:[]
};



const PullRequestSlice = createSlice({
  name: "open", // this name using in action type
  initialState, //this is initial state
  reducers: {
    getPullRequests: (state, action) => {
      console.log("GELEN DEĞER", action.payload)
      state.pullRequest = action.payload;
      state.userNames = splitUsers(action.payload);
      state.allUser = getAllUsers(state.userNames, action.payload)
      console.log("state openpr", state.pullRequest)
    },
  },
});

export const PullRequestReducer =  PullRequestSlice.reducer;
export const { getPullRequests } = PullRequestSlice.actions;