const API = "";

let customersData = [];
let vehiclesData = [];
let slotsData = [];
let transactionsData = [];


/* =========================
   DASHBOARD
========================= */

function refreshDashboard() {

    loadCustomers();

    loadVehicles();

    loadSlots();

    loadTransactions();

}


/* =========================
   CUSTOMERS
========================= */

function addCustomer() {

    const name =
        document.getElementById("customerName").value.trim();

    const phone =
        document.getElementById("customerPhone").value.trim();

    const email =
        document.getElementById("customerEmail").value.trim();


    if (!name) {

        alert("Enter customer name");

        return;

    }


    fetch(API + "/customers", {

        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify({

            name: name,
            phone: phone,
            email: email

        })

    })

        .then(response => {

            if (!response.ok) {

                throw new Error("Customer creation failed");

            }

            return response.json();

        })

        .then(() => {

            alert("Customer added successfully");

            document.getElementById("customerName").value = "";

            document.getElementById("customerPhone").value = "";

            document.getElementById("customerEmail").value = "";

            loadCustomers();

        })

        .catch(error => {

            alert(error.message);

        });

}


function loadCustomers() {

    fetch(API + "/customers")

        .then(response => response.json())

        .then(data => {

            customersData = data;

            document.getElementById("customerCount")
                .innerText = data.length;


            const table =
                document.getElementById("customerTable");

            table.innerHTML = "";


            data.forEach(customer => {

                table.innerHTML += `

                <tr>

                    <td>${customer.id}</td>

                    <td>${customer.name || "-"}</td>

                    <td>${customer.phone || "-"}</td>

                    <td>${customer.email || "-"}</td>

                    <td>

                        <button
                            class="delete-button"
                            onclick="deleteCustomer(${customer.id})">

                            Delete

                        </button>

                    </td>

                </tr>

                `;

            });

        })

        .catch(error => {

            console.error(error);

        });

}


function deleteCustomer(id) {

    if (!confirm("Delete this customer?")) {
        return;
    }


    fetch(API + "/customers/" + id, {

        method: "DELETE"

    })

        .then(() => {

            loadCustomers();

        });

}



/* =========================
   VEHICLES
========================= */

function addVehicle() {

    const vehicleNumber =
        document.getElementById("vehicleNumber").value.trim();

    const vehicleType =
        document.getElementById("vehicleType").value;

    const customerId =
        document.getElementById("vehicleCustomerId").value;


    if (!vehicleNumber || !customerId) {

        alert("Enter vehicle number and customer ID");

        return;

    }


    fetch(API + "/vehicles", {

        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify({

            vehicleNumber: vehicleNumber,

            vehicleType: vehicleType,

            customer: {
                id: Number(customerId)
            }

        })

    })

        .then(response => {

            if (!response.ok) {

                throw new Error("Vehicle creation failed");

            }

            return response.json();

        })

        .then(() => {

            alert("Vehicle added successfully");

            document.getElementById("vehicleNumber").value = "";

            document.getElementById("vehicleCustomerId").value = "";

            loadVehicles();

        })

        .catch(error => {

            alert(error.message);

        });

}


function loadVehicles() {

    fetch(API + "/vehicles")

        .then(response => response.json())

        .then(data => {

            vehiclesData = data;

            document.getElementById("vehicleCount")
                .innerText = data.length;


            const table =
                document.getElementById("vehicleTable");

            table.innerHTML = "";


            data.forEach(vehicle => {

                let customer = "-";

                if (vehicle.customer) {

                    customer =
                        vehicle.customer.name ||
                        vehicle.customer.id;

                }


                table.innerHTML += `

                <tr>

                    <td>${vehicle.id}</td>

                    <td>${vehicle.vehicleNumber || "-"}</td>

                    <td>${vehicle.vehicleType || "-"}</td>

                    <td>${customer}</td>

                    <td>

                        <button
                            class="delete-button"
                            onclick="deleteVehicle(${vehicle.id})">

                            Delete

                        </button>

                    </td>

                </tr>

                `;

            });

        })

        .catch(error => {

            console.error(error);

        });

}


function deleteVehicle(id) {

    if (!confirm("Delete this vehicle?")) {
        return;
    }


    fetch(API + "/vehicles/" + id, {

        method: "DELETE"

    })

        .then(() => {

            loadVehicles();

        });

}



/* =========================
   SLOTS
========================= */

function addSlot() {

    const slotNumber =
        document.getElementById("slotNumber").value.trim();

    const slotType =
        document.getElementById("slotType").value;


    if (!slotNumber) {

        alert("Enter slot number");

        return;

    }


    fetch(API + "/slots", {

        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify({

            slotNumber: slotNumber,

            slotType: slotType,

            available: true

        })

    })

        .then(response => {

            if (!response.ok) {

                throw new Error("Slot creation failed");

            }

            return response.json();

        })

        .then(() => {

            alert("Slot added successfully");

            document.getElementById("slotNumber").value = "";

            loadSlots();

        })

        .catch(error => {

            alert(error.message);

        });

}


function loadSlots() {

    fetch(API + "/slots")

        .then(response => response.json())

        .then(data => {

            slotsData = data;

            updateSlotDashboard(data);

            displaySlotCards(data);

            displaySlotTable(data);

        })

        .catch(error => {

            console.error(error);

        });

}


function updateSlotDashboard(slots) {

    const total = slots.length;

    const available =
        slots.filter(slot => slot.available === true).length;

    const occupied =
        total - available;


    document.getElementById("slotCount")
        .innerText = total;


    document.getElementById("availableSlotCount")
        .innerText = available;


    document.getElementById("overviewAvailable")
        .innerText = available;


    document.getElementById("overviewOccupied")
        .innerText = occupied;


    if (total > 0) {

        const percentage =
            Math.round((occupied / total) * 100);

        document.getElementById("occupiedPercentage")
            .innerText = percentage + "%";

    } else {

        document.getElementById("occupiedPercentage")
            .innerText = "0%";

    }

}


function displaySlotCards(slots) {

    const container =
        document.getElementById("slotCards");

    container.innerHTML = "";


    slots.forEach(slot => {

        const available =
            slot.available === true;


        container.innerHTML += `

            <div class="slot-card
                ${available
            ? "available-slot"
            : "occupied-slot"}">

                <div class="slot-number">

                    ${slot.slotNumber}

                </div>

                <div class="slot-type">

                    ${slot.slotType || "General"}

                </div>

                <div class="slot-status
                    ${available
            ? "available-text"
            : "occupied-text"}">

                    ${available
            ? "AVAILABLE"
            : "OCCUPIED"}

                </div>

            </div>

        `;

    });

}


function displaySlotTable(slots) {

    const table =
        document.getElementById("slotTable");

    table.innerHTML = "";


    slots.forEach(slot => {

        const available =
            slot.available === true;


        table.innerHTML += `

            <tr>

                <td>${slot.id}</td>

                <td>${slot.slotNumber}</td>

                <td>${slot.slotType || "-"}</td>

                <td>

                    <span class="status-badge
                        ${available
            ? "status-completed"
            : "status-active"}">

                        ${available
            ? "AVAILABLE"
            : "OCCUPIED"}

                    </span>

                </td>

                <td>

                    <button
                        class="delete-button"
                        onclick="deleteSlot(${slot.id})">

                        Delete

                    </button>

                </td>

            </tr>

        `;

    });

}


function deleteSlot(id) {

    if (!confirm("Delete this slot?")) {
        return;
    }


    fetch(API + "/slots/" + id, {

        method: "DELETE"

    })

        .then(() => {

            loadSlots();

        });

}



/* =========================
   PARKING ALLOCATION
========================= */

function allocateParking() {

    const vehicleId =
        document.getElementById("allocateVehicleId").value;


    if (!vehicleId) {

        alert("Enter vehicle ID");

        return;

    }


    fetch(
        API + "/parking/allocate/" + vehicleId,
        {
            method: "POST"
        }
    )

        .then(response => {

            if (!response.ok) {

                return response.text()
                    .then(text => {

                        throw new Error(
                            text || "Allocation failed"
                        );

                    });

            }

            return response.json();

        })

        .then(transaction => {

            const slot =
                transaction.slot
                    ? transaction.slot.slotNumber
                    : "-";


            document.getElementById(
                "allocationMessage"
            ).innerText =
                "Parking allocated successfully. Slot: "
                + slot;


            document.getElementById(
                "allocateVehicleId"
            ).value = "";


            loadSlots();

            loadTransactions();

        })

        .catch(error => {

            document.getElementById(
                "allocationMessage"
            ).innerText =
                "Error: " + error.message;

        });

}



/* =========================
   TRANSACTIONS
========================= */

function loadTransactions() {

    fetch(API + "/parking/transactions")

        .then(response => response.json())

        .then(data => {

            transactionsData = data;

            calculateRevenue(data);

            displayRecentTransactions(data);

            displayFullTransactions(data);

        })

        .catch(error => {

            console.error(error);

        });

}


function calculateRevenue(transactions) {

    let revenue = 0;


    transactions.forEach(transaction => {

        if (transaction.fee != null) {

            revenue += Number(transaction.fee);

        }

    });


    document.getElementById("totalRevenue")
        .innerText =
        revenue.toFixed(2);

}


function displayRecentTransactions(transactions) {

    const table =
        document.getElementById("transactionTable");


    table.innerHTML = "";


    const recent =
        transactions.slice(-8).reverse();


    recent.forEach(transaction => {

        const vehicle =
            transaction.vehicle || {};

        const slot =
            transaction.slot || {};


        const active =
            !transaction.exitTime;


        table.innerHTML += `

            <tr>

                <td>
                    #${transaction.id}
                </td>

                <td>
                    <strong>
                        ${vehicle.vehicleNumber || "-"}
                    </strong>
                </td>

                <td>
                    ${vehicle.vehicleType || "-"}
                </td>

                <td>
                    ${slot.slotNumber || "-"}
                </td>

                <td>
                    ${formatDate(transaction.entryTime)}
                </td>

                <td>
                    ${transaction.exitTime
            ? formatDate(transaction.exitTime)
            : "-"}
                </td>

                <td>
                    ₹${transaction.fee || 0}
                </td>

                <td>

                    <span class="status-badge
                        ${active
            ? "status-active"
            : "status-completed"}">

                        ${active
            ? "PARKED"
            : "COMPLETED"}

                    </span>

                </td>

            </tr>

        `;

    });

}


function displayFullTransactions(transactions) {

    const table =
        document.getElementById(
            "fullTransactionTable"
        );


    if (!table) {
        return;
    }


    table.innerHTML = "";


    transactions.forEach(transaction => {

        const vehicle =
            transaction.vehicle || {};

        const slot =
            transaction.slot || {};


        table.innerHTML += `

            <tr>

                <td>
                    #${transaction.id}
                </td>

                <td>
                    ${vehicle.vehicleNumber || "-"}
                </td>

                <td>
                    ${vehicle.vehicleType || "-"}
                </td>

                <td>
                    ${slot.slotNumber || "-"}
                </td>

                <td>
                    ${formatDate(transaction.entryTime)}
                </td>

                <td>
                    ${transaction.exitTime
            ? formatDate(transaction.exitTime)
            : "Still Parked"}
                </td>

                <td>
                    ₹${transaction.fee || 0}
                </td>

                <td>

                    ${
            transaction.exitTime
                ?
                `
                        <span class="status-badge status-completed">
                            Completed
                        </span>
                        `
                :
                `
                        <button
                            class="exit-button"
                            onclick="exitVehicle(${transaction.id})">

                            Vehicle Exit

                        </button>
                        `
        }

                </td>

            </tr>

        `;

    });

}


function formatDate(value) {

    if (!value) {
        return "-";
    }

    return value
        .replace("T", " ")
        .substring(0, 19);

}



/* =========================
   VEHICLE EXIT
========================= */

function exitVehicle(transactionId) {

    if (!confirm("Confirm vehicle exit?")) {
        return;
    }


    fetch(
        API + "/parking/exit/" + transactionId,
        {
            method: "PUT"
        }
    )

        .then(response => {

            if (!response.ok) {

                return response.text()
                    .then(text => {

                        throw new Error(
                            text || "Vehicle exit failed"
                        );

                    });

            }

            return response.json();

        })

        .then(() => {

            alert(
                "Vehicle exit completed successfully"
            );


            loadSlots();

            loadTransactions();

        })

        .catch(error => {

            alert(
                "Error: " + error.message
            );

        });

}



/* =========================
   NAVIGATION
========================= */

document.querySelectorAll(".nav-item")
    .forEach(item => {

        item.addEventListener("click", function() {

            document
                .querySelectorAll(".nav-item")
                .forEach(nav => {

                    nav.classList.remove("active");

                });


            this.classList.add("active");


            const target =
                this.getAttribute("href")
                    .substring(1);


            document
                .querySelectorAll(".page-section")
                .forEach(section => {

                    section.classList.add(
                        "hidden-section"
                    );

                });


            const targetSection =
                document.getElementById(target);


            if (targetSection) {

                targetSection.classList.remove(
                    "hidden-section"
                );

            }

        });

    });



/* =========================
   INITIAL LOAD
========================= */

window.addEventListener(
    "load",
    function() {

        loadCustomers();

        loadVehicles();

        loadSlots();

        loadTransactions();

    }
);