import { configureStore } from "@reduxjs/toolkit";
import { applyMiddleware } from 'redux'

import {OpenPullRequestReducer} from "./open_true/OpenPullRequestSlice"
import {DialogScreenReducer} from "./dialog/dialogSlice"
import customizationReducer from '../store/customizationReducer';
import FullScreenDialog from "views/utilities/FullScreenDialog";


const middlewareEnhancer = applyMiddleware(FullScreenDialog)

const reduxStore = configureStore({
  reducer: {
    openPR:OpenPullRequestReducer,
    customization: customizationReducer,
    dialogScreen:DialogScreenReducer
  },
});

export default reduxStore;