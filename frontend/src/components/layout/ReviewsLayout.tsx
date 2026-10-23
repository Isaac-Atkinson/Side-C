import PageContainer from "./pageContainer.tsx";
import {NavLink, Outlet, useLocation} from "react-router-dom";
import {Box, MenuItem, Select, Tab, Tabs, Typography} from "@mui/material";
import {useState} from "react";
import { SORT_OPTIONS } from "./SortOptions.tsx";



function ReviewsLayout(){
    const {pathname} = useLocation();
    const currentTab = pathname.includes("/albums") ? "albums" : "artists";

    const [sortBy, setSortBy] = useState(SORT_OPTIONS.CREATED_NEWEST);

    return (
        <PageContainer>

            <Box
                sx={{
                    bgcolor: "background.paper",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "space-between",
                }}
            >
                <Tabs value={currentTab} textColor="primary" indicatorColor="primary">
                    <Tab label="Albums" value="albums" component={NavLink} to="albums" />
                    <Tab label="Artists" value="artists" component={NavLink} to="artists" />
                </Tabs>

                <Box
                    sx={{
                        display: "flex",
                        alignItems: "center",
                        gap: 0.5,
                    }}
                >
                    <Typography variant="body1">
                        Sort by:
                    </Typography>

                    <Select
                        value={sortBy}
                        onChange={(e) => setSortBy(e.target.value)}
                    >
                        <MenuItem value = {SORT_OPTIONS.CREATED_NEWEST}>
                            Created: Newest
                        </MenuItem>
                        <MenuItem value = {SORT_OPTIONS.CREATED_OLDEST}>
                            Created: Oldest
                        </MenuItem>
                        <MenuItem value = {SORT_OPTIONS.LISTENED_NEWEST}>
                            Listened: Newest
                        </MenuItem>
                        <MenuItem value = {SORT_OPTIONS.LISTENED_OLDEST}>
                            Listened: Oldest
                        </MenuItem>
                        <MenuItem value={SORT_OPTIONS.HIGHEST_RATED}>
                            Rating: Highest
                        </MenuItem>

                        <MenuItem value={SORT_OPTIONS.LOWEST_RATED}>
                            Rating: Lowest
                        </MenuItem>
                        <MenuItem value={SORT_OPTIONS.ALPHABETICAL_AZ}>
                            Alphabetical: A-Z
                        </MenuItem>

                        <MenuItem value={SORT_OPTIONS.ALPHABETICAL_ZA}>
                            Alphabetical: Z-A
                        </MenuItem>
                    </Select>
                </Box>
            </Box>


            <Outlet context={{sortBy}}/>
        </PageContainer>
    )
}

export default ReviewsLayout;