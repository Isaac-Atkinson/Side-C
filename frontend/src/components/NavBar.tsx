import {AppBar, Box, Button, Toolbar} from "@mui/material";
import {Link, useNavigate} from "react-router-dom";
import SearchBar from "./SearchBar.tsx";
import {useUserContext} from "../context/UserContext.tsx";


function NavBar() {
    const {username, loading, logout} = useUserContext();
    const navigate = useNavigate();

    const handleLogout = async () => {
        await logout();
        navigate("/login");
    }

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
                        color="inherit"

                    >
                        Reviews
                    </Button>

                </Box>


                <Box sx={{ ml: "auto" }}>

                    {!loading && ( username ? (
                        <Button
                            variant="contained"
                            onClick={handleLogout}
                        >
                            Logout
                        </Button>
                    ) : (
                        <Button
                            variant="contained"
                            component={Link} to="/login"
                            sx = {{
                                bgcolor: "actionButton.main"
                            }}
                        >
                            Login
                        </Button>
                    ))}

                </Box>

            </Toolbar>


        </AppBar>
        )
}

export default NavBar;