terraform {
  required_providers {
    random = {
      source = "hashicorp/random"
      version = "~> 3.6.2"
    }
  }
}

provider "aws" {
  region = local.aws_region
}

provider "random" {
}

resource "random_pet" "random_name" {
  length    = 2
  separator = "-"
}

variable "account_id" {
  description = "AWS Account ID"
  default     = "932043840972"
}

# S3 bucket
resource "aws_s3_bucket" "lecture_bucket" {
    bucket        = "lecture-bucket-${random_pet.random_name.id}"
    tags = {
        Name = "Lecture Bucket"
        Environment = "Development"
    }
    force_destroy = true
    lifecycle {
        prevent_destroy = true
    }
}

# save generated bucket name to properties file
resource "local_file" "properties_file" {
  content = <<-EOT
    lecture_bucket=${aws_s3_bucket.lecture_bucket.bucket}
  EOT
  depends_on = [aws_s3_bucket.lecture_bucket]

  filename = "../services/media/src/main/resources/buckets.properties"
}