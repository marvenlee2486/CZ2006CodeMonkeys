import { createSlice } from '@reduxjs/toolkit'

export const userDataSlice = createSlice({
  name: 'userdata',
  initialState: {
    data: [],
  },
  reducers: {
    updateUsers: (state,action) => {
        state.data = action.payload
    }
  },
})

// Action creators are generated for each case reducer function
export const {updateUsers} = userDataSlice.actions

export default userDataSlice.reducer