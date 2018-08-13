External libraries used:
	- RecyclerView
		- pros: solves most of the memory and performance issues caused by utilizing ListView poorly
		- cons: unsure
	- CardView
		- pros: easy to implement material items in a list/grid
		- cons: has some unexpected differences in results when using padding/margins pre-Lollipop
	- RetroFit w/ Gson and Scalar converters
		- pros: simple means to interact with HTTPS REST calls (Scalar converters used for testing)
		- cons: unsure since I am relatively new to using the library
	- Glide
		- pros: simple image loading via URI/URL with caching handled automatically
		- cons: perhaps less full-featured than Fresco but simpler to use
	- Dagger
		- pros: dependency injection improves code readability and maintability
		- cons: unsure since this was the first project where I have used the library
		
		
Assumptions, limitations, and next steps:
	- currently the API call to fetch recent images is not paginated, so only one batch of photos is retrieved
		- adding pagination could be added on backend
	- like status is not persisted when the app is killed since there is currently no simple way to determine whether an account has liked a photo
		- we could persist likes to disk, but this would only semi-solve the problem since like status would not persist across reinstalls or installing on a new device
		- could add an API call that would return all photo ids a user has liked
	- "id_token" is assumed to be the name of the authorization token in perpetuity
		- do not know if I am extracting "id_token" through standard means from login success redirect since this is my first time using WebView
	- all photos are assumed to have a low resolution and normal resolution URL
	- all photos are assumed to have a similar aspect ratio
	- could add more animation to initializing the photo list and scrolling through the photo list
	- logging out does not invalidate any currently un-expired auth token, but we delete the locally cached token to prevent any issue in the future should this be implemented
	- do not have xxhdpi icons for large heart and logout avatar since icons8.com does not allow downloading of custom-sized icons greater than 100x100 pixels
	