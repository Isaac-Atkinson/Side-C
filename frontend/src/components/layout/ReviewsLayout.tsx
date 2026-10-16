import PageContainer from "./pageContainer.tsx";
import {NavLink, Outlet} from "react-router-dom";
import {Button} from "@mui/material";

function ReviewsLayout(){
    return (
        <PageContainer>

            <Button color="inherit" component={NavLink} to="albums" sx={{
                "&[aria-current='page']": {
                    bgcolor: "primary.main",
                },
            }}>
                Albums
            </Button>

            <Button color="inherit" component={NavLink} to="artists" sx={{
                "&[aria-current='page']": {
                    bgcolor: "primary.main",
                },
            }}>
                Artists
            </Button>

            <Outlet />
        </PageContainer>
    )
}

export default ReviewsLayout;