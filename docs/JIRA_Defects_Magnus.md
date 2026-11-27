# JALA Academy Magnus — 5 Verified JIRA Defect Reports

All 5 defects below were directly reproduced and verified on the live application (`https://magnus.jalatechnologies.com/`) using Selenium WebDriver and Chrome DevTools inspection.

---

## Defect 1: MAG-101 — Employee Search Fails to Filter Non-Matching Queries & Renders Blank Gender Column
- **Issue Type:** Bug
- **Project:** Magnus (`MAG`)
- **Component / Module:** Employee -> Search (`/Employee/Search`)
- **Severity:** Major / High
- **Priority:** P1 (High)
- **Environment:** Chrome 157 / Windows 11 / `https://magnus.jalatechnologies.com/Employee/Search`
- **Summary:** Employee Search (`#btnSearch`) displays all default records even for non-existent employee names, and the `Gender` column renders blank for employee rows.
- **Steps to Reproduce:**
  1. Log in to Magnus using `training@jalaacademy.com` / `jobprogram`.
  2. Navigate to **Employee → Search** (`/Employee/Search`).
  3. Observe the `Gender` column (`td[5]`) in the results table (`#tblEmployee`).
  4. Enter a non-existent employee name `NONEXISTENT_XYZ_999` in the `Name` field (`#Name`).
  5. Click the **Search** button (`#btnSearch`).
- **Expected Result:**
  1. Searching for a non-existent employee should filter the table and display `0` rows or a `"No matching records found"` empty-state message.
  2. The `Gender` column should display `"Male"` or `"Female"` for every employee record.
- **Actual Result:**
  1. After clicking **Search** with `NONEXISTENT_XYZ_999`, `#tblEmployee tbody tr` still renders **10 default employee rows** (starting with `ARUN KUMAR SEELAM`).
  2. The `Gender` column cell is completely empty (`""`) across rows.
- **Evidence:** Verified via live DOM audit (`defect_search_gender_and_dupid.png`).

---

## Defect 2: MAG-102 — "In Window Popup" Modal Fails to Open on Popups Page
- **Issue Type:** Bug
- **Project:** Magnus (`MAG`)
- **Component / Module:** More -> Popups (`/Home/Popup`)
- **Severity:** Major
- **Priority:** P2 (High)
- **Environment:** Chrome 157 / Windows 11 / `https://magnus.jalatechnologies.com/Home/Popup`
- **Summary:** Clicking the **"In Window Popup"** button (`#btn-six`) does not display the Bootstrap modal dialog (`#myModal1`).
- **Steps to Reproduce:**
  1. Log in to Magnus and navigate to **More → Popups** (`/Home/Popup`).
  2. Click the **In Window Popup** button (`a#btn-six` with attributes `data-toggle="modal" data-target="#myModal1"`).
- **Expected Result:**
  - The Bootstrap modal `#myModal1` titled `"Popup One"` should open visibly inside the current browser window with a body message and a **Close** button.
- **Actual Result:**
  - Clicking `#btn-six` triggers no visible modal dialog; `#myModal1` remains hidden (`display: none`, `isDisplayed() == false`).
- **Evidence:** Verified via Selenium check (`defect_in_window_popup.png`).

---

## Defect 3: MAG-103 — "Other City" Input Remains Hidden When Checkbox Is Checked & Invalid Employee Fields Lack Inline Validation
- **Issue Type:** Bug
- **Project:** Magnus (`MAG`)
- **Component / Module:** Employee -> Create (`/Employee/Create`)
- **Severity:** Major
- **Priority:** P1 (High)
- **Environment:** Chrome 157 / Windows 11 / `https://magnus.jalatechnologies.com/Employee/Create`
- **Summary:** Checking the **"Other City"** checkbox (`#chkOtherCity`) fails to display `#divOtherCity` / `#OtherCity`, and submitting invalid Email, Mobile No, and DOB formats displays zero inline field validation errors.
- **Steps to Reproduce:**
  1. Log in to Magnus and navigate to **Employee → Create** (`/Employee/Create`).
  2. Check the **Other City** checkbox (`#chkOtherCity`). Observe whether `#OtherCity` appears.
  3. Enter `FirstName`: `TestDefect`, `LastName`: `User999`.
  4. Enter invalid `EmailId`: `not_valid_email`, invalid `MobileNo`: `12`, and invalid `DOB`: `99/99/9999`.
  5. Click **Save**.
- **Expected Result:**
  1. Checking `#chkOtherCity` should toggle `#divOtherCity` from `display:none` to visible so the user can enter a custom city name.
  2. Inline validation errors should appear under Email (format), Mobile No (10 digits), and Date of Birth (`dd/mm/yyyy`) preventing invalid submission.
- **Actual Result:**
  1. `#divOtherCity` remains `style="display:none;"` (`isDisplayed() == false`) even after `#chkOtherCity` is checked.
  2. No inline validation error text appears inside `.field-validation-error` or `.text-danger` spans when invalid Email/Mobile/DOB values are submitted.
- **Evidence:** Verified via live DOM inspection (`defect_emp_create_validation.png`).

---

## Defect 4: MAG-104 — Broken/403 Links, Malformed Relative URL, and Mixed-Content HTTP iFrames
- **Issue Type:** Bug
- **Project:** Magnus (`MAG`)
- **Component / Module:** More -> Links (`/Home/Links`) & More -> iFrames (`/Home/Iframe`)
- **Severity:** Medium
- **Priority:** P2 (Medium)
- **Environment:** Chrome 157 / Windows 11 / HTTPS Magnus origin
- **Summary:** The `Working Links` tab includes a 403 Forbidden link, the `Broken Links` tab uses a malformed protocol-less `href`, and `/Home/Iframe` embeds insecure `http://` URLs inside an `https://` page.
- **Steps to Reproduce:**
  1. Navigate to **More → Links** (`/Home/Links`).
  2. Check `Link 1` under **Working Links** (`http://www.workinglinks.co.uk/`).
  3. Switch to **Broken Links** (`#tab_2`) and inspect `Link 1` (`href="www.brokenlinkcheck.com/"`).
  4. Navigate to **More → iFrames** (`/Home/Iframe`) and inspect `#iframe1` and `#iframe3`.
- **Expected Result:**
  1. All links under **Working Links** should return HTTP `200 OK`.
  2. External links should include `https://` protocol rather than resolving relative to `/Home/`.
  3. All embedded iframes on an HTTPS page should use `https://` sources to avoid Mixed Content browser blocking.
- **Actual Result:**
  1. `http://www.workinglinks.co.uk/` under Working Links returns **HTTP 403 Forbidden**.
  2. `href="www.brokenlinkcheck.com/"` resolves incorrectly as `https://magnus.jalatechnologies.com/Home/www.brokenlinkcheck.com/`.
  3. `#iframe1` (`http://www.seleniumhq.org/`) and `#iframe3` (`http://www.youtube.com/embed/m7dSteMFhZI`) use plain `http://` on an `https://` page, causing Mixed Content blocking; additionally `/Home/AutoComplete` throws `Uncaught TypeError: Cannot read properties of undefined (reading 'addMethod')` in browser console.
- **Evidence:** Verified via Selenium HTTP & console audit (`defect_iframe_mixed_content.png`).

---

## Defect 5: MAG-105 — Duplicate DOM IDs on Employee Search Pagination, UI Spelling Typos, and Mismatched Login Footer URL
- **Issue Type:** Bug
- **Project:** Magnus (`MAG`)
- **Component / Module:** Global UI / Login / Home / Employee Create & Search
- **Severity:** Minor / Medium
- **Priority:** P3 (Medium)
- **Environment:** Chrome 157 / Windows 11
- **Summary:** Multiple UI spelling errors, duplicate HTML element IDs on the pagination bar, and mismatched anchor text vs `href` destination on the Login page.
- **Steps to Reproduce:**
  1. Inspect `/Home/Index` text body and `/Employee/Create` section labels.
  2. Inspect `/` (Login page) footer link text and `href`.
  3. Run `document.querySelectorAll('[id]')` duplicate check on `/Employee/Search`.
- **Expected Result:**
  1. Zero spelling errors across UI headings/labels (`Skills`, `Search`, `get`, `completely`, `characters`).
  2. Every HTML `id` attribute on a page must be unique per W3C specification.
  3. Link display text must match its target `href` destination.
- **Actual Result:**
  1. **Spelling Errors:** `"Skiils"` on `/Employee/Create`; `"Seach on Google"`, `"gat a software job"`, `"completley"` on `/Home/Index`; `"less than 20 charcters"` in validation message on `#OtherCity`.
  2. **Duplicate IDs (5 pairs):** On `/Employee/Search`, `liFirst_EmployeeGrid`, `liPrevious_EmployeeGrid`, `txtPageNumber_EmployeeGrid`, `liNext_EmployeeGrid`, and `liLast_EmployeeGrid` each appear **twice** in the DOM.
  3. **Mismatched Link:** On `/`, the footer link text reads `"https://jalaacademy.com/"` while its `href` navigates to `"http://jalatechnologies.com/contact-us.html"`.
- **Evidence:** Verified via live DOM inspection (`defect_home_typos.png`, `defect_emp_create_skiils_and_validation.png`).
