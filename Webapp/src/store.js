import { configureStore } from '@reduxjs/toolkit'
import userdataReducer from './features/userData'

export default configureStore({
  reducer: {
    userdata: userdataReducer,
  },
})