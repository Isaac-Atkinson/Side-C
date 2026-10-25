import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import ReviewForm from "../components/forms/review/ReviewForm.tsx";
import {userEvent} from "@testing-library/user-event/dist/cjs/setup/index.js";
import {LocalizationProvider} from "@mui/x-date-pickers/LocalizationProvider";
import {AdapterDayjs} from "@mui/x-date-pickers/AdapterDayjs";


describe("ReviewForm", () => {


    it("shows an error when submitted without a rating", async () => {

        const user = userEvent.setup();

        render(
            <ReviewForm
                title="Discovery"
                placeholder={<div />}
                onSubmit={vi.fn()}
                onCancel={vi.fn()}
            />
        );



        const submitButton = screen.getByRole("button", {
            name: /submit/i,
        });

        await user.click(submitButton);

        expect(
            await screen.findByText("Rating is required")
        ).toBeInTheDocument();
    });

    it("shows listened on field for album reviews", async () => {

        render(
            <LocalizationProvider dateAdapter={AdapterDayjs}>
                <ReviewForm
                    title="Discovery"
                    placeholder={<div />}
                    onSubmit={vi.fn()}
                    onCancel={vi.fn()}
                    showListenedOn={true}
                />
            </LocalizationProvider>
        );



        expect(
            screen.getByRole("button", {
                name: /choose date/i,
            })
        ).toBeInTheDocument()
    })


    it("Does not show listened on field for artist reviews", async () => {

        render(
            <ReviewForm
                title="Discovery"
                placeholder={<div />}
                onSubmit={vi.fn()}
                onCancel={vi.fn()}
                showListenedOn={false}
            />
        );

        expect(
            screen.queryByLabelText("Listened on")
        ).not.toBeInTheDocument()
    })

    it("Expected values are submitted", async () => {

        const user = userEvent.setup();
        const onSubmit = vi.fn().mockResolvedValue(null);

        render(
            <LocalizationProvider dateAdapter={AdapterDayjs}>
                <ReviewForm
                    title="Discovery"
                    placeholder={<div />}

                    existingRating={10}
                    existingReviewText={"Great album"}
                    existingListenedOn={"2026-10-23"}

                    onSubmit={onSubmit}
                    onCancel={vi.fn()}
                    showListenedOn={true}
                />
            </LocalizationProvider>
        );


        const submitButton = screen.getByRole("button", {
            name: /submit/i,
        });

        await user.click(submitButton);

        expect(onSubmit).toHaveBeenCalledWith(
            10, 'Great album', '2026-10-23'
        );

    })
});