
import type {ArtistReview} from "../../types/ArtistReview.ts";
import {Link} from "react-router-dom";
import {Card, CardContent, CardMedia, Typography} from "@mui/material";

type ArtistDataProps = {
    review : ArtistReview;
};

function ArtistReviewCard({review}: ArtistDataProps) {

    return (
        <Card
            component={Link}
            to={`/artists/${review.id}`}
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
                image={review.artist.imageUrl}
                alt={`${review.artist.name} image`}
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
                    {review.artist.name}
                </Typography>

                <Typography
                    variant="body1"
                    sx={{ mt: 1 }}
                >
                    {review.rating}/10
                </Typography>
            </CardContent>
        </Card>
    );
}

export default ArtistReviewCard