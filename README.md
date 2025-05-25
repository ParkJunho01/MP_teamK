# Tag-Eat: Restaurant Recommendation Map App

**Tag-Eat** is an Android application that helps users search for and discover restaurants using the Naver Map SDK and Geocoding API.  
Users can enter a location keyword to place a marker on the map and view restaurant details via a bottom sheet panel.

---

## 📌 Key Features

- 🔍 **Location Search (Geocoding)**: Enter keywords to find matching locations via Naver Geocoding API
- 📍 **Dynamic Markers**: Automatically display map markers based on search results
- 🧾 **Bottom Sheet Info Panel**: Tap a marker to view restaurant details in a BottomSheetDialogFragment
- ✨ **Autocomplete Suggestions**: Improve search accuracy with predefined or dynamic keyword suggestions

---

## 🔧 Tech Stack

- Android Studio (Java)
- Naver Map SDK
- Naver Geocoding API
- Material Components (UI)
- OkHttp (HTTP networking)

---

## 🗺️ API Integration

- **Geocoding API**: Convert location keywords into map coordinates
- **(Optional)** Local Search API: Fetch nearby restaurants using coordinates

> API keys are managed locally and are not committed to public repositories.

---

## 📂 Project Structure

📁 fragment/

├── HomeFragment.java

├── RestaurantBottomSheet.java

📁 model/

├── RestaurantModel.java

📁 res/layout/

├── fragment_home.xml

├── bottom_sheet_restaurant.xml


yaml

복사

편집

---

## 🚀 Planned Features

- 🔄 Firebase integration (user favorites, login)
- 📸 Restaurant images, ratings, and user reviews
- 📡 Auto-search based on user GPS location
