import PageContainer from "./pageContainer.tsx";
import {NavLink, Outlet, useLocation} from "react-router-dom";
import {Box, Tab, Tabs} from "@mui/material";

function ReviewsLayout(){
    const {pathname} = useLocation();
    const currentTab = pathname.includes("/albums") ? "albums" : "artists";

    return (
        <PageContainer>

            <Box
                sx = {{
                    bgcolor: "background.paper",
                }}
            >
                <Tabs value={currentTab} textColor="primary" indicatorColor="primary">
                    <Tab label="Albums" value="albums" component={NavLink} to="albums" />
                    <Tab label="Artists" value="artists" component={NavLink} to="artists" />
                </Tabs>
            </Box>

            <Outlet />
        </PageContainer>
    )
}

export default ReviewsLayout;