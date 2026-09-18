

import type {AlbumReview} from "../../types/AlbumReview.ts";
import {Link} from "react-router-dom";
import {
    Card,
    CardContent,
    CardMedia,
    Typography
} from "@mui/material";


type AlbumDataProps = {
    albumReview : AlbumReview;
};



function AlbumCard({albumReview }: AlbumDataProps){


    return (

            <Card
                component={Link}
                to={`/albums/${albumReview.id}`}
                sx={{
                    width: 220,
                    cursor: "pointer",
                    bgcolor: "#181818",
                    color: "common.white",
                    transition: "0.2s",
                    textDecoration: "none",


                    "&:hover": {
                        bgcolor: "#202020",
                        transform: "translateY(-3px)",
                        boxShadow: 6
                    }
                }}
            >
                <CardMedia

                    component="img"
                    height="220"
                    image={albumReview.album.imageUrl}
                    alt={`${albumReview.album.title} cover`}
                />

                <CardContent>
                    <Typography

                        variant="h6"
                        noWrap
                        sx={{
                            color: "inherit",
                            textDecoration: "none",

                            "&:hover": {

                            }
                        }}
                    >
                        {albumReview.album.title}
                    </Typography>

                    <Typography
                        variant="body2"
                        sx={{ color: "#a7a7a7" }}
                        noWrap
                    >
                        {albumReview.album.artist.name}
                    </Typography>

                    <Typography
                        variant="body1"
                        sx={{ mt: 1 }}
                    >
                        {albumReview.rating}/10
                    </Typography>
                </CardContent>
            </Card>
    );
}

export default AlbumCard;