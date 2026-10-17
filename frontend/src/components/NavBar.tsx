import {AppBar, Box, Button, Toolbar} from "@mui/material";
import {Link} from "react-router-dom";
import SearchBar from "./SearchBar.tsx";


function NavBar() {

    return (
        <AppBar
            position="static"
        >
            <Toolbar>
                <Box sx={{ display: "flex", alignItems: "center", gap: 2 }}>
                    <SearchBar />

                    <Button
                        component={Link} to="/reviews/albums"
                        variant="contained"

                    >
                        Reviews
                    </Button>

                </Box>


                <Box sx={{ ml: "auto" }}>


                    <Button
                        variant="contained"
                        component={Link} to="/login"
                        sx = {{
                            bgcolor: "actionButton.main"
                        }}
                    >
                        Login
                    </Button>
                </Box>

            </Toolbar>


        </AppBar>
        )
}

export default NavBar;