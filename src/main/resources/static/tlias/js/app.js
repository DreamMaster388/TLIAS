const PAGE_SIZE = 10;

const app = Vue.createApp({
    data() {
        return {
            employees: [],
            total: 0,
            departments: [],
            addPositions: [],
            editPositions: [],
            addDeptId: null,
            editDeptId: null,
            searchName: '',
            searchGender: '',
            searchDepartment: '',
            searchPosition: '',
            searchMinSalary: '',
            searchMaxSalary: '',
            searchMinEntryDate: '',
            searchMaxEntryDate: '',
            currentPage: 1,
            jumpPage: null,
            showSearchModal: false,
            deleteTarget: null,
            showDeleteModal: false,
            showAddModal: false,
            showEditModal: false,
            editTarget: null,
            addForm: {
                name: '',
                gender: '男',
                deptPosId: null,
                salary: null,
                entryDate: ''
            },
            editForm: {
                name: '',
                gender: '男',
                deptPosId: null,
                salary: null,
                entryDate: ''
            },
        }
    },
    computed: {
        totalPages() {
            return Math.ceil(this.total / PAGE_SIZE);
        },
        pageData() {
            return this.employees;
        }
    },
    methods: {
        openSearchModal() {
            this.showSearchModal = true;
        },
        closeSearchModal() {
            this.showSearchModal = false;
        },
        fetchDepartments() {
            fetch('/api/departments')
                .then(res => res.json())
                .then(data => { this.departments = data; });
        },
        fetchAddPositions() {
            if (!this.addDeptId) { this.addPositions = []; return; }
            fetch(`/api/departments/${this.addDeptId}/positions`)
                .then(res => res.json())
                .then(data => { this.addPositions = data; });
        },
        fetchEditPositions() {
            if (!this.editDeptId) { this.editPositions = []; return; }
            fetch(`/api/departments/${this.editDeptId}/positions`)
                .then(res => res.json())
                .then(data => { this.editPositions = data; });
        },
        onAddDeptChange() {
            this.addForm.deptPosId = null;
            this.fetchAddPositions();
        },
        onEditDeptChange() {
            this.editForm.deptPosId = null;
            this.fetchEditPositions();
        },
        fetchEmployees() {
            const params = new URLSearchParams();
            if (this.searchName) params.append('name', this.searchName);
            if (this.searchGender) params.append('gender', this.searchGender);
            if (this.searchDepartment) params.append('department', this.searchDepartment);
            if (this.searchPosition) params.append('position', this.searchPosition);
            if (this.searchMinSalary) params.append('minSalary', this.searchMinSalary);
            if (this.searchMaxSalary) params.append('maxSalary', this.searchMaxSalary);
            if (this.searchMinEntryDate) params.append('minEntryDate', this.searchMinEntryDate);
            if (this.searchMaxEntryDate) params.append('maxEntryDate', this.searchMaxEntryDate);
            params.append('page', this.currentPage);
            params.append('size', PAGE_SIZE);

            fetch(`/api/employees?${params.toString()}`)
                .then(res => res.json())
                .then(result => {
                    if (result.code === 1) {
                        this.employees = result.data.data;
                        this.total = result.data.total;
                    }
                });
        },
        clear() {
            this.searchName = '';
            this.searchGender = '';
            this.searchDepartment = '';
            this.searchPosition = '';
            this.searchMinSalary = '';
            this.searchMaxSalary = '';
            this.searchMinEntryDate = '';
            this.searchMaxEntryDate = '';
        },
        search() {
            this.currentPage = 1;
            this.showSearchModal = false;
            this.fetchEmployees();
        },
        goPage(p) {
            if (typeof p !== 'number' || p < 1 || p > this.totalPages) return;
            this.currentPage = p;
            this.fetchEmployees();
        },
        jumpToPage() {
            const p = this.jumpPage;
            if (p === null || p === '' || isNaN(p)) return;
            this.goPage(p);
            this.jumpPage = null;
        },
        deleteRow(d) {
            this.deleteTarget = d;
            this.showDeleteModal = true;
        },
        confirmDelete() {
            const id = this.deleteTarget.id;
            fetch(`/api/employees/${id}`,
            { method: 'DELETE' })
                .then(() => {
                    this.showDeleteModal = false;
                    this.deleteTarget = null;
                    this.fetchEmployees();
                });
        },
        cancelDelete() {
            this.showDeleteModal = false;
            this.deleteTarget = null;
        },
        openEditModal(d) {
            this.editTarget = d;
            this.editForm = { name: d.name, gender: d.gender, deptPosId: d.deptPosId, salary: d.salary, entryDate: d.entryDate };
            const dept = this.departments.find(dep => dep.name === d.department);
            this.editDeptId = dept ? dept.id : null;
            if (this.editDeptId) {
                fetch(`/api/departments/${this.editDeptId}/positions`)
                    .then(res => res.json())
                    .then(data => {
                        this.editPositions = data;
                    });
            }
            this.showEditModal = true;
        },
        closeEditModal() {
            this.showEditModal = false;
        },
        editEmployee() {
            const id = this.editTarget.id;
            fetch(`/api/employees/${id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(this.editForm)
            }).then(() => {
                this.showEditModal = false;
                this.fetchEmployees();
            });
        },
        openAddModal() {
            this.addForm = { name: '', gender: '男', deptPosId: null, salary: null, entryDate: '' };
            this.addDeptId = null;
            this.addPositions = [];
            this.showAddModal = true;
        },
        closeAddModal() {
            this.showAddModal = false;
        },
        saveEmployee() {
            if (!this.addForm.name) return;
            fetch('/api/employees', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(this.addForm)
            }).then(() => {
                this.showAddModal = false;
                this.fetchEmployees();
            });
        }
    },
    mounted() {
        this.fetchEmployees();
        this.fetchDepartments();
    }
});

app.mount('#app');
