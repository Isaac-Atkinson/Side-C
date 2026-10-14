import {
    TextField
} from "@mui/material";
import {useState} from "react";
import {useNavigate} from "react-router-dom";

function SearchBar() {
    const[search, setSearch] = useState("");

    const navigate = useNavigate();


    const handleSubmit = (e: React.SubmitEvent) => {
        e.preventDefault();

        if (!search.trim()) return;

        navigate(`/search?query=${encodeURIComponent(search.trim())}`);
        setSearch("");
    };


    return (
        <form onSubmit={handleSubmit}>
            <TextField
                label="Search"
                value={search}
                onChange={(e) => setSearch(e.target.value)}
            />
        </form>
    )
}

export default SearchBar;