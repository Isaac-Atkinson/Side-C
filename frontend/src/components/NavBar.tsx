import {AppBar, Box, Button, Toolbar} from "@mui/material";
import {Link} from "react-router-dom";
import SearchBar from "./SearchBar.tsx";


function NavBar() {

    return (
        <AppBar position="static">
            <Toolbar>
                <Box sx={{ display: "flex", alignItems: "center", gap: 2 }}>
                    <Button color="inherit" component={Link} to="/albums">
                        Albums
                    </Button>

                    <Button color="inherit" component={Link} to="/artists">
                        Artists
                    </Button>

                    <Button color="inherit" component={Link} to="/login">
                        Login
                    </Button>
                </Box>


                <Box sx={{ ml: "auto" }}>
                    <SearchBar />
                </Box>

            </Toolbar>


        </AppBar>
        )
}

export default NavBar;