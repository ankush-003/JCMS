import requests
import json
import random

url = "http://localhost:8080/api/comments"

token = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJhcnlhIiwic3ViIjoiaW5zYW5lX2hlaXNlbmJlcmdAZ21haWwuY29tIiwiZXhwIjoxNzEzODE2MzE2LCJpYXQiOjE3MTM4MTQ1MTYsInNjb3BlIjoiUkVBRCBERUxFVEUgVVBEQVRFIFdSSVRFIn0.LyzaCmxHLm8BM6arB5xFvZLuq0LuE_QM5AkooUW9F9ZMDXyIjg2VO1bFS3yX5C19YWhdY2HIQ5vaOmkGG6Ql1WiyUyWn6-horwvHD4Vl1yXvhukV9gkzljzC7Is0XqGHdsCNpmtXasYZRCIplsxhP3Kz4X67g9obl9VLnqEXZXTBC6cH4dM2_bsgc67RA1l5sacJhx_e9TLsOghn_47JHpUpJ1R2tWUGRgmJ5o2JTzHhVANPgn8Bd4_5xPDyAqOCBeaKT73jo5bIEja3mMtu5rJLdFXt8Mmvii_siGa029ZxaKvsAMS0J2dQRWBWsFICI0OfjSichpvHUSiDdbNzJw"

appreciation_comments = [
    "Great post!",
    "Well done!",
    "Awesome content!",
    "I love this!",
    "Fantastic!",
    "Brilliant!",
    "You nailed it!",
    "Keep up the good work!",
    "Impressive!",
    "Amazing!",
    "This is gold!",
    "So good!",
    "Absolutely wonderful!",
    "Thank you for sharing!",
    "I'm inspired!",
    "You deserve all the upvotes!",
    "Incredible work!",
    "This made my day!",
    "So insightful!",
    "This deserves more attention!",
    "This is what Reddit is all about!",
    "I'm blown away!",
    "You're awesome!",
    "Mind-blowing!",
    "This is why I love Reddit!",
    "Outstanding!",
    "This needs to go viral!",
    "You've got talent!",
    "Keep sharing your brilliance!",
    "This is top-notch!",
    "I'm speechless!",
    "Thank you for brightening my day!",
    "You're a legend!",
    "This is legendary!",
    "You're on fire!",
    "You're killing it!",
    "This is pure gold!",
    "You've outdone yourself!",
    "This is next level!",
    "This deserves an award!",
    "This is epic!",
    "I'm in awe!",
    "I'm impressed beyond words!",
    "This is genius!",
    "You're a genius!",
    "This is masterful!",
    "You're a master!",
    "This is exceptional!",
    "You're exceptional!",
    "This is extraordinary!",
    "You're extraordinary!",
    "This is phenomenal!",
    "You're phenomenal!",
    "This is remarkable!",
    "You're remarkable!",
    "This is outstanding!",
    "You're outstanding!",
    "This is superb!",
    "You're superb!",
    "This is terrific!",
    "You're terrific!",
    "This is fantastic!",
    "You're fantastic!",
    "This is amazing!",
    "You're amazing!",
    "This is incredible!",
    "You're incredible!",
    "This is awesome!",
    "You're awesome!",
    "This is great!",
    "You're great!",
    "This is excellent!",
    "You're excellent!",
    "This is wonderful!",
    "You're wonderful!",
    "This is perfect!",
    "You're perfect!",
    "This is splendid!",
    "You're splendid!",
    "This is superb!",
    "You're superb!",
    "This is fabulous!",
    "You're fabulous!",
    "This is sensational!",
    "You're sensational!",
    "This is stellar!",
    "You're stellar!",
    "This is top-notch!",
    "You're top-notch!",
    "This is first-rate!",
    "You're first-rate!",
    "This is premium!",
    "You're premium!",
    "This is exceptional!",
    "You're exceptional!",
    "This is phenomenal!",
    "You're phenomenal!",
    "This is incredible!",
    "You're incredible!",
    "This is fantastic!",
    "You're fantastic!",
    "This is amazing!",
    "You're amazing!"
]


for x in range(1552,1596 + 1):
    
    # choose a number between 3 and 8
    num_comments = random.randint(3,5)
    
    for i in range(1,num_comments + 1):
        payload = json.dumps({
        "description": random.choice(appreciation_comments),
        "user": {
            "id": random.randint(1152, 1631)
        },
        "post": {
            "id": x
        }
        })
        headers = {
        'Content-Type': 'application/json',
        'Authorization': f'Bearer {token}'
        }

        response = requests.request("POST", url, headers=headers, data=payload)

        if response.status_code == 200:
            print(f"Added comment {i} to Post {x}")
        else:
            print("Failed")
            continue
