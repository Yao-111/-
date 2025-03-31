from django.contrib.auth.models import AbstractUser

class CustomUser(AbstractUser):
    USER_ROLES = (
        ('student', '学生'),
        ('teacher', '教师'),
        ('institution', '教育机构'),
        ('admin', '管理员')
    )
    mobile = models.CharField(max_length=20)
    role = models.CharField(max_length=20, choices=USER_ROLES, default='student')
    registration_date = models.DateTimeField(auto_now_add=True)