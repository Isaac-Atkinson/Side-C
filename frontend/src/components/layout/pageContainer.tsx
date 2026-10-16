import {Container} from "@mui/material";
import type {ReactNode} from "react";

function PageContainer({ children }: { children: ReactNode }) {
    return (
        <Container
            maxWidth={false}
            sx={{
                py: 4,
                px: 4
        }}
        >
            {children}
        </Container>
    );
}

export default PageContainer;