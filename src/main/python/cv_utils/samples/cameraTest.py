import cv2

cap = cv2.VideoCapture(0)

while 1:
    ret, frame = cap.read()
    if ret:
        cv2.circle(frame, (h / 2, w / 2), 7, (0, 255, 0), -1)
        cv2.imshow(str(frame.shape), frame)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()
