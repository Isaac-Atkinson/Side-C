import {Box, Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, Rating, Typography} from "@mui/material";
import {useState, type ReactNode} from "react";
import dayjs from "dayjs";


type DetailedReviewCardProps = {
    title: string;
    subtitle?: string;

    rating: number;
    reviewText?: string;
    listenedOn?: string
    showListenedOn: boolean

    imageUrl?: string;
    imageAlt: string;
    placeholder: ReactNode;

    onEdit: () => void;
    onDelete: () => void;
}

function DetailedReviewCard({title, subtitle, rating, reviewText, listenedOn, showListenedOn, imageUrl, imageAlt, placeholder, onDelete, onEdit}: DetailedReviewCardProps) {
    const [confirmingDelete, setConfirmingDelete] = useState(false);

    return (
        <Box
            sx={{
                width: "100%",
                maxWidth: 900,
                mx: "auto",
                bgcolor: "background.paper",
                border: 1,
                borderColor: "divider",
                borderRadius: 2,
                p:3
            }}
        >

            <Box
                sx={{
                    display: "flex",
                    alignItems: "stretch",
                    gap: 3,
                }}
            >

                {imageUrl ? (
                    <Box
                        component="img"
                        src={imageUrl}
                        alt={imageAlt}
                        sx={{
                            width: "30%",
                            aspectRatio: "1 / 1",
                            objectFit: "cover",
                            borderRadius: 2,
                            flexShrink: 0,
                            alignSelf: "flex-start"
                        }}
                    />
                ) : (
                    <Box
                        sx={{
                            width: "30%",
                            aspectRatio: "1 / 1",
                            borderRadius: 2,
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

                <Box
                    sx={{
                        display: "flex",
                        flexDirection: "column",
                        flex: 1,
                        minWidth: 0
                    }}
                >
                    <Typography variant="h4" >
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

                    <Box
                        sx={{
                            display:"flex",
                            flexDirection: "row",
                            gap: 1.5,
                            mt: 1
                        }}
                    >


                        <Rating
                            value={Number(rating) / 2}
                            precision={0.5}
                            size= "large"
                            readOnly
                        />

                        {showListenedOn && (
                            <Typography
                                variant = "h6"
                                color="textSecondary"
                                sx={{fontWeight: "normal", mt:"auto"}}
                            >
                                {"Listened: " + dayjs(listenedOn).format("DD/MM/YYYY")}
                            </Typography>
                        )}

                    </Box>

                    {reviewText && (
                        <Typography
                            variant="body1"
                            sx={{
                                whiteSpace: "pre-wrap",
                                overflowWrap: "break-word",
                                mt: 2,
                                mb: 2
                            }}
                        >
                            {reviewText}
                        </Typography>
                    )}

                    <Box sx={{
                        display: "flex",
                        justifyContent: "flex-end",
                        gap: 1,
                        mt: "auto",


                    }}>
                        <Button variant="contained" onClick ={onEdit}>
                            Edit
                        </Button>

                        <Button variant="outlined" color="error" onClick={() => setConfirmingDelete(true)}>
                            Delete
                        </Button>
                    </Box>

                </Box>


            </Box>

            <Dialog
                open={confirmingDelete}
                onClose={() => setConfirmingDelete(false)}
            >
                <DialogTitle >Delete review</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Are you sure you want to delete this review?
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button autoFocus onClick={() => setConfirmingDelete(false)}>
                        Cancel
                    </Button>
                    <Button variant="contained" color="error" onClick={() => {
                        setConfirmingDelete(false);
                        onDelete();
                    }}>
                        Delete
                    </Button>
                </DialogActions>
            </Dialog>
        </Box>


    )
}

export default DetailedReviewCard;
