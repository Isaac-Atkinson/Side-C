import {Box, Button, Rating, Typography} from "@mui/material";
import type {ReactNode} from "react";


type DetailedReviewCardProps = {
    title: string;
    subtitle?: string;
    rating: number;
    reviewText?: string;
    imageUrl?: string;
    imageAlt: string;
    placeholder: ReactNode;

    onEdit: () => void;
    onDelete: () => void;
}

function DetailedReviewCard({title, subtitle, rating, reviewText, imageUrl, imageAlt, placeholder, onDelete, onEdit}: DetailedReviewCardProps) {




    return (
        <Box
            sx={{
                width: "60%",
                mx: "auto",
                bgcolor: "background.paper",
                border: 1,
                borderColor: "divider",
                borderRadius: 2,
            }}
        >
            <Box
                sx={{
                    width: "100%",
                    maxWidth: 900,
                    mx: "auto",
                    p: 3,
                }}
            >
                <Box
                    sx={{
                        display: "flex",
                        gap: 4,
                        alignItems: "flex-start"
                    }}
                >

                    {imageUrl ? (
                        <Box
                            component="img"
                            src={imageUrl}
                            alt={imageAlt}
                            sx={{
                                width: "30%",
                                borderRadius: 2
                            }}
                        />
                    ) : (
                        <Box
                            sx={{
                                width: "30%",
                                aspectRatio: "1 / 1",
                                borderRadius: 1,
                                flexShrink: 0,
                                bgcolor: "action.hover",
                                display: "flex",
                                alignItems: "center",
                                justifyContent: "center",
                            }}
                        >
                            {placeholder}
                        </Box>
                    )}


                    <Box sx={{ flex: 1 }}>
                        <Typography variant="h3">
                            {title}
                        </Typography>

                        {subtitle && (
                            <Typography
                                variant="h6"
                                color={"textSecondary"}
                            >
                                {subtitle}
                            </Typography>
                        )}

                        <Rating
                            value={Number(rating) / 2}
                            precision={0.5}
                            size= "large"
                            readOnly
                        />
                    </Box>



                </Box>

                <Box
                    sx={{
                        display: "flex",
                        flexDirection: "column",
                        gap : 1,
                        mt: 2
                    }}
                >

                    {reviewText && (
                        <Box
                            sx={{
                                display: "flex",
                                flexDirection: "column",
                                gap: 2,
                                p: 1,
                            }}
                        >


                            <Typography
                                variant="body1"
                                sx={{
                                    whiteSpace: "pre-wrap",
                                    overflowWrap: "break-word",
                                }}
                            >
                                {reviewText}
                            </Typography>
                        </Box>
                    )}




                    <Box sx={{
                        display: "flex",
                        justifyContent: "flex-end",
                        gap: 1,
                        mt: 4,

                    }}>
                        <Button variant="contained" onClick ={onEdit}>
                            Edit
                        </Button>

                        <Button variant="outlined" color="error" onClick={onDelete}>
                            Delete
                        </Button>
                    </Box>
                </Box>

            </Box>

        </Box>


    )
}

export default DetailedReviewCard;