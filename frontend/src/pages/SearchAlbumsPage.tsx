import {useState} from "react";
import {Box, Button, TextField} from "@mui/material";
import {API_BASE_URL} from "../config/api.ts";
import type {AlbumSearchResult} from "../types/AlbumSearchResult.ts";


function SearchAlbumsPage(){
    const[search, setSearch] = useState("");
    const[results, setResults] = useState<AlbumSearchResult[]>([]);



    const handleSearch = async () =>{
        const response = await fetch(
            `${API_BASE_URL}/search/albums?title=${encodeURIComponent(search)}`)

        if(response.ok){
            const data : AlbumSearchResult[] = await response.json();
            setResults(data);
        } else if(response.status === 503){
            console.log(response.statusText);
        }


    }
    return(
        <div>
            <Box>
                <TextField
                    label="Search albums"
                    value={search}
                    onChange={e => setSearch(e.target.value)}
                />

                <Button variant="contained" onClick = {handleSearch}>
                    Search
                </Button>

                {results.map(album => (
                    <div key={album.musicBrainzId}>
                        <h3>{album.title}</h3>
                        <p>{album.artist}</p>
                    </div>
                ))}
            </Box>
        </div>
    )
}

export default SearchAlbumsPage;