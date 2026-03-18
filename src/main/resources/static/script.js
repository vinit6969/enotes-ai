async function uploadNote() {

    let title = document.getElementById("title").value.trim();
    let text = document.getElementById("text").value.trim();
    let file = document.getElementById("file").files[0];

    let loading = document.getElementById("loading");
    let result = document.getElementById("result");
    let btn = document.getElementById("btn");

    // 🔴 VALIDATION
    if (!title) {
        result.innerText = "Title is required";
        return;
    }

    if (!text && !file) {
        result.innerText = "Provide text or upload PDF";
        return;
    }

    if (file && file.type !== "application/pdf") {
        result.innerText = "Only PDF files allowed";
        return;
    }

    // Show spinner
    loading.style.display = "block";
    btn.disabled = true;
    result.innerText = "";

    let formData = new FormData();
    formData.append("title", title);

    if (file) {
        formData.append("file", file);
    } else {
        formData.append("text", text);
    }

    try {
        let response = await fetch("http://localhost:8080/api/notes/upload", {
            method: "POST",
            body: formData
        });

        if (!response.ok) {
            throw new Error("Server error");
        }

        let data = await response.json();

        if (data.summaryText) {
            result.innerText = data.summaryText;
        } else {
            result.innerText = "No summary generated";
        }

    } catch (error) {
        result.innerText = "Failed to process. Try again.";
    }

    loading.style.display = "none";
    btn.disabled = false;
}

let btn = document.getElementById("btn");

// before API call
btn.disabled = true;

// after API call (at end)
btn.disabled = false;