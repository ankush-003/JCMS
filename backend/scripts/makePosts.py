import requests
import json
import random



# token = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJhcnlhIiwic3ViIjoiaW5zYW5lX2hlaXNlbmJlcmdAZ21haWwuY29tIiwiZXhwIjoxNzEzODE0MzA4LCJpYXQiOjE3MTM4MTI1MDgsInNjb3BlIjoiUkVBRCBERUxFVEUgVVBEQVRFIFdSSVRFIn0.qEWf4Nc_DAdmNcG31utL1h0Mr3WqYS_UwKlx14qCTmwJDNrK6Aw2Ds5V84Vvzm1p-n4g34ExbbF-5-wNC9f0a7YF_UgguFxXEGkyhtUI02OGdICF0UQK06k-2nk0ifgCmxugBTnuzM1-9caUZlGj2KSraViY98aum12GXqNvNo_YE-TdYvAoENASDojf-NKLN01uljm2U9sAxAW6N_7UY4s_DsRQCxAQQ3kRhHbxDViOlsrvOSLEkdkTAYxfkYSc7G6LUaFSl2UEXC4ctylLkfCk_wlDXNMniWc6bNf4dRAOOsN7Ossp43ehE3ROGulf76kMoaQ3lscvZz-4fds1wQ"

token = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJhcnlhIiwic3ViIjoiaW5zYW5lX2hlaXNlbmJlcmdAZ21haWwuY29tIiwiZXhwIjoxNzEzODE2MzE2LCJpYXQiOjE3MTM4MTQ1MTYsInNjb3BlIjoiUkVBRCBERUxFVEUgVVBEQVRFIFdSSVRFIn0.LyzaCmxHLm8BM6arB5xFvZLuq0LuE_QM5AkooUW9F9ZMDXyIjg2VO1bFS3yX5C19YWhdY2HIQ5vaOmkGG6Ql1WiyUyWn6-horwvHD4Vl1yXvhukV9gkzljzC7Is0XqGHdsCNpmtXasYZRCIplsxhP3Kz4X67g9obl9VLnqEXZXTBC6cH4dM2_bsgc67RA1l5sacJhx_e9TLsOghn_47JHpUpJ1R2tWUGRgmJ5o2JTzHhVANPgn8Bd4_5xPDyAqOCBeaKT73jo5bIEja3mMtu5rJLdFXt8Mmvii_siGa029ZxaKvsAMS0J2dQRWBWsFICI0OfjSichpvHUSiDdbNzJw"

def getAllChannels():
	url = "http://localhost:8080/api/channels"
	
	headers = {}
	headers['Authorization'] = 'Bearer ' + token
	response = requests.request("GET", url, headers=headers)
	return response.json()




def MakePosts():
	channels = getAllChannels()
 
	for channel in channels:  
		for i in range(1,6):
			userID = random.randint(1152, 1631)
			url = "http://localhost:8080/api/posts"
			payload = {
				"description": f"Example Post Description {i}", 
				"title": f"Example Post Title {i}",
				"user": {
					"id": userID
				},
				"channel": {
					"id": channel['id']
				}
			}
			headers = {
       		"Content-Type": "application/json",
			'Authorization': 'Bearer ' + token,
			}
			response = requests.request("POST", url, headers=headers, json=payload)
			if response.status_code == 200:
				print(f"Created Post {i} in Channel {channel['name']}")
			else:
				print(response.text)
				continue
    
 

MakePosts()