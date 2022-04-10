// import
import Dashboard from "views/Dashboard.js";
import Administrative from "views/Tables.js";
import SignIn from "views/SignIn.js";
import NewPassword from "views/NewPassword";

import {
  HomeIcon,
  DocumentIcon,
  SupportIcon,
} from "components/Icons/Icons";

var dashRoutes = [
  {
    path: "/pages",
    name: "Dashboard",
    rtlName: "لوحة القيادة",
    icon: <HomeIcon color="inherit" />,
    component: Dashboard,
    layout: "/admin",
  },
  {
    path: "/pages",
    name: "Administrative",
    rtlName: "لوحة القيادة",
    icon: <SupportIcon color="inherit" />,
    component: Administrative,
    layout: "/admin",
  },
  {
    name: "ACCOUNT PAGES",
    category: "account",
    rtlName: "صفحات",
    state: "pageCollapse",
    views: [
      {
        path: "/signin",
        name: "Sign In",
        rtlName: "لوحة القيادة",
        icon: <DocumentIcon color="inherit" />,
        component: SignIn,
        layout: "/auth",
      },
      {
        path: "/newpassword",
        name: "New Password",
        rtlName: "لوحة القيادة",
        icon: <DocumentIcon color="inherit" />,
        component: NewPassword,
        layout: "/auth",
      },
    ],
  },
];
export default dashRoutes;
