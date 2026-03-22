# MongoDB Schema

## users

{
  id
  fullName
  email
  phone
  password
  timezone
  profileImage
  bio
  createdAt
}

## contacts

{
  id
  userId
  contactUserId
  addedAt
}

## messages

{
  id
  senderId
  receiverId
  message
  messageType (TEXT, FILE)
  fileUrl
  createdAt
  readStatus
}

## files

{
  id
  senderId
  receiverId
  fileName
  fileUrl
  fileSize
  uploadedAt
}