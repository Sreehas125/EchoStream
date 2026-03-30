const outputPanel = document.getElementById("outputPanel");
const apiStatus = document.getElementById("apiStatus");

function parseForm(form) {
    const formData = new FormData(form);
    const data = {};
    for (const [key, value] of formData.entries()) {
        if (value === "") {
            continue;
        }
        if (key.toLowerCase().includes("id") || key.includes("Seconds") || key.includes("Number") || key.includes("Count")) {
            data[key] = Number(value);
        } else if (key === "grossRevenue" || key === "fixedRatePerStream" || key === "revenueSharePercent" || key === "amount") {
            data[key] = Number(value);
        } else {
            data[key] = value;
        }
    }
    return data;
}

function writeOutput(title, payload) {
    const stamp = new Date().toLocaleTimeString();
    outputPanel.textContent = `[${stamp}] ${title}\n${JSON.stringify(payload, null, 2)}`;
}

async function callApi(url, method, body) {
    const options = {
        method,
        headers: {
            "Content-Type": "application/json"
        }
    };

    if (body) {
        options.body = JSON.stringify(body);
    }

    const response = await fetch(url, options);
    let payload;
    try {
        payload = await response.json();
    } catch (_error) {
        payload = { message: await response.text() };
    }

    if (!response.ok) {
        throw { status: response.status, payload };
    }

    return { status: response.status, payload };
}

async function withOutput(title, action) {
    try {
        const result = await action();
        writeOutput(`${title} - SUCCESS (${result.status})`, result.payload);
    } catch (error) {
        writeOutput(`${title} - FAILED (${error.status || "N/A"})`, error.payload || { message: "Unexpected error" });
    }
}

async function checkApiStatus() {
    try {
        const response = await fetch("/");
        if (!response.ok) {
            throw new Error();
        }
        apiStatus.textContent = "API Reachable";
        apiStatus.classList.add("ok");
        apiStatus.classList.remove("bad");
    } catch (_error) {
        apiStatus.textContent = "API Not Reachable";
        apiStatus.classList.add("bad");
        apiStatus.classList.remove("ok");
    }
}

document.getElementById("ingestionForm").addEventListener("submit", async (event) => {
    event.preventDefault();
    const body = parseForm(event.target);
    await withOutput("Ingestion", () => callApi("/api/ingestion/tracks", "POST", body));
});

document.getElementById("licenseForm").addEventListener("submit", async (event) => {
    event.preventDefault();
    const body = parseForm(event.target);
    const { trackId, ...licenseBody } = body;
    await withOutput("License Issue", () => callApi(`/api/rights/tracks/${trackId}/licenses`, "POST", licenseBody));
});

document.getElementById("fixedRoyaltyForm").addEventListener("submit", async (event) => {
    event.preventDefault();
    const body = parseForm(event.target);
    await withOutput("Royalty Fixed", () => callApi("/api/royalties/calculate/fixed-rate", "POST", body));
});

document.getElementById("shareRoyaltyForm").addEventListener("submit", async (event) => {
    event.preventDefault();
    const body = parseForm(event.target);
    await withOutput("Royalty Share", () => callApi("/api/royalties/calculate/revenue-share", "POST", body));
});

document.getElementById("transactionForm").addEventListener("submit", async (event) => {
    event.preventDefault();
    const body = parseForm(event.target);
    await withOutput("Royalty Payout", () => callApi("/api/royalties/transactions", "POST", body));
});

document.getElementById("auditForm").addEventListener("click", async (event) => {
    const action = event.target.getAttribute("data-action");
    if (!action) {
        return;
    }

    const form = document.getElementById("auditForm");
    const body = parseForm(form);
    if (!body.trackId) {
        writeOutput("Audit Action - FAILED", { message: "Track ID is required" });
        return;
    }

    let path;
    if (action === "assign") {
        if (!body.auditorId) {
            writeOutput("Audit Assign - FAILED", { message: "Auditor ID is required for assignment" });
            return;
        }
        path = `/api/audit/tracks/${body.trackId}/assign/${body.auditorId}`;
    } else {
        path = `/api/audit/tracks/${body.trackId}/${action}`;
    }

    await withOutput(`Audit ${action.toUpperCase()}`, () => callApi(path, "PUT"));
});

checkApiStatus();
