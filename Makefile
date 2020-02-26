.PHONY: build_docker
build_docker:
	mvn clean package docker:build

.PHONY: run_docker
run_docker:
	docker-compose -f docker/common/docker-compose.yml up


.PHONY: cui_test
cui_test:
	curl -XGET http://localhost:8080/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a/licenses/f3831f8c-c338-4ebe-a82a-e2fc1d1ff78a | json_pp
	curl -XGET http://localhost:8080/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a/licenses/f3831f8c-c338-4ebe-a82a-e2fc1d1ff78a/feign | json_pp
	curl -XGET http://localhost:8080/health | json_pp
	curl -XGET http://localhost:8888/licensingservice/default | json_pp
	curl -XGET http://localhost:8080/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a/licenses/ | json_pp
	curl -XGET -H 'tmx-correlation-id:TEST-CORRELATION-ID' http://localhost:8080/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a/licenses/ | json_pp
	curl -XGET http://localhost:5555/api/licensingservice/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a/licenses/ | json_pp